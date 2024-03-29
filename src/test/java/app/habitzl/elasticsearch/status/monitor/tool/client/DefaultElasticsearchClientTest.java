package app.habitzl.elasticsearch.status.monitor.tool.client;

import app.habitzl.elasticsearch.status.monitor.ClusterInfos;
import app.habitzl.elasticsearch.status.monitor.ClusterNodeInfos;
import app.habitzl.elasticsearch.status.monitor.ClusterSettingsUtils;
import app.habitzl.elasticsearch.status.monitor.Randoms;
import app.habitzl.elasticsearch.status.monitor.UnassignedShardInfos;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.cluster.ClusterSettings;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.connection.ConnectionStatus;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.ClusterNodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.node.NodeInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.data.shard.UnassignedShardInfo;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterAllocationParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterHealthParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterSettingsParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterStateParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.ClusterStatsParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.EndpointVersionParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.GeneralParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeInfoParams;
import app.habitzl.elasticsearch.status.monitor.tool.client.params.NodeStatsParams;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.BasicStatusLine;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAnd;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultElasticsearchClientTest {

    private static final ProtocolVersion HTTP_PROTOCOL_VERSION = new ProtocolVersion("HTTP", 2, 0);
    private static final RequestLine FAKE_REQUEST_LINE = new BasicRequestLine("GET", "/", HTTP_PROTOCOL_VERSION);
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_NOT_FOUND = 404;

    private DefaultElasticsearchClient sut;
    private RestClient client;
    private ResponseMapper responseMapper;
    private InfoMapper infoMapper;

    @BeforeEach
    void setUp() {
        client = mock(RestClient.class);
        responseMapper = mock(ResponseMapper.class);
        infoMapper = mock(InfoMapper.class);
        sut = new DefaultElasticsearchClient(client, responseMapper, infoMapper);
    }

    @Test
    void checkConnection_endpointReachable_sendEndpointVersionRequest() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        sut.checkConnection();

        // Then
        Request expectedRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, EndpointVersionParams.API_ENDPOINT);
        verify(client).performRequest(expectedRequest);
    }

    @Test
    void checkConnection_successResponse_returnSuccessConnectionInfo() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        ConnectionInfo result = sut.checkConnection();

        // Then
        ConnectionInfo expected = ConnectionInfo.success();
        assertThat(result, equalTo(expected));
    }

    @Test
    void checkConnection_errorResponse_returnErrorConnectionInfo() throws IOException {
        // Given
        Response response = givenClientRespondsWith(HTTP_STATUS_NOT_FOUND);

        // When
        ConnectionInfo result = sut.checkConnection();

        // Then
        ConnectionInfo expected = ConnectionInfo.error(
                ConnectionStatus.fromHttpCode(HTTP_STATUS_NOT_FOUND),
                response.getStatusLine().toString()
        );
        assertThat(result, equalTo(expected));
    }

    @Test
    void checkConnection_clientThrowsResponseException_returnErrorConnectionInfo() throws IOException {
        // Given
        ResponseException exception = givenClientThrowsResponseException(HTTP_STATUS_NOT_FOUND);

        // When
        ConnectionInfo result = sut.checkConnection();

        // Then
        ConnectionInfo expected = ConnectionInfo.error(
                ConnectionStatus.fromHttpCode(HTTP_STATUS_NOT_FOUND),
                exception.getResponse().getStatusLine().toString()
        );
        assertThat(result, equalTo(expected));
    }

    @Test
    void checkConnection_clientThrowsSSLHandshakeException_returnErrorConnectionInfo() throws IOException {
        // Given
        SSLHandshakeException exception = givenClientThrowsSSLHandshakeException();

        // When
        ConnectionInfo result = sut.checkConnection();

        // Then
        ConnectionInfo expected = ConnectionInfo.error(
                ConnectionStatus.SSL_HANDSHAKE_FAILURE,
                exception.getMessage()
        );
        assertThat(result, equalTo(expected));
    }

    @Test
    void checkConnection_clientThrowsIOException_returnErrorConnectionInfo() throws IOException {
        // Given
        IOException exception = givenClientThrowsIOException();

        // When
        ConnectionInfo result = sut.checkConnection();

        // Then
        ConnectionInfo expected = ConnectionInfo.error(
                ConnectionStatus.NOT_FOUND,
                exception.getMessage()
        );
        assertThat(result, equalTo(expected));
    }

    @Test
    void getClusterSettings_always_sendClusterSettingsRequest() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        sut.getClusterSettings();

        // Then
        Request expectedRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterSettingsParams.API_ENDPOINT);
        expectedRequest.addParameter(ClusterSettingsParams.PARAM_INCLUDE_DEFAULTS, "true");
        verify(client).performRequest(expectedRequest);
    }

    @Test
    void getClusterSettings_successResponse_returnClusterSettings() throws IOException {
        // Given
        Response response = givenClientRespondsWith(HTTP_STATUS_OK);
        String content = givenResponseHasContent(response);
        ClusterSettings expected = givenContentCanBeMappedToClusterSettings(content);

        // When
        Optional<ClusterSettings> result = sut.getClusterSettings();

        // Then
        assertThat(result, isPresentAnd(equalTo(expected)));
    }

    @Test
    void getClusterSettings_responseMapperThrowsException_returnEmpty() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);
        givenResponseMapperThrowsException();

        // When
        Optional<ClusterSettings> result = sut.getClusterSettings();

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getClusterInfo_always_sendClusterHealthAndClusterStateAndClusterStatsRequests() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        sut.getClusterInfo();

        // Then
        Request expectedClusterHealthRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterHealthParams.API_ENDPOINT);
        Request expectedClusterStateRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterStateParams.API_ENDPOINT);
        Request expectedClusterStatsRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterStatsParams.API_ENDPOINT);

        verify(client).performRequest(expectedClusterHealthRequest);
        verify(client).performRequest(expectedClusterStateRequest);
        verify(client).performRequest(expectedClusterStatsRequest);
    }

    @Test
    void getClusterInfo_successResponse_returnClusterInfo() throws IOException {
        // Given
        Response response = givenClientRespondsWith(HTTP_STATUS_OK);
        String content = givenResponseHasContent(response);
        ClusterInfo expected = givenContentCanBeMappedToClusterInfo(content);

        // When
        Optional<ClusterInfo> result = sut.getClusterInfo();

        // Then
        assertThat(result, isPresentAnd(equalTo(expected)));
    }

    @Test
    void getClusterInfo_responseMapperThrowsException_returnEmpty() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);
        givenResponseMapperThrowsException();

        // When
        Optional<ClusterInfo> result = sut.getClusterInfo();

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getNodeInfo_always_sendClusterStateAndNodeInfoAndNodeStatsRequests() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        sut.getNodeInfo();

        // Then
        Request expectedMasterNodeRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterStateParams.onlyRequestMasterNode());
        Request expectedNodeInfoRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, NodeInfoParams.API_ENDPOINT);
        Request expectedNodeStatsRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, NodeStatsParams.API_ENDPOINT);
        expectedNodeStatsRequest.addParameter(NodeStatsParams.PARAM_METRIC, NodeStatsParams.allMetrics());

        verify(client).performRequest(expectedMasterNodeRequest);
        verify(client).performRequest(expectedNodeInfoRequest);
        verify(client).performRequest(expectedNodeStatsRequest);
    }

    @Test
    void getNodeInfo_successResponse_returnClusterInfo() throws IOException {
        // Given
        Response response = givenClientRespondsWith(HTTP_STATUS_OK);
        String content = givenResponseHasContent(response);
        List<NodeInfo> expectedNodeInfos = givenContentCanBeMappedToNodeInfo(content);

        // When
        Optional<ClusterNodeInfo> result = sut.getNodeInfo();

        // Then
        ClusterNodeInfo expected = new ClusterNodeInfo(expectedNodeInfos);
        assertThat(result, isPresentAnd(equalTo(expected)));
    }

    @Test
    void getNodeInfo_responseMapperThrowsException_returnEmpty() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);
        givenResponseMapperThrowsException();

        // When
        Optional<ClusterNodeInfo> result = sut.getNodeInfo();

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getUnassignedShardInfo_always_sendClusterAllocationRequest() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);

        // When
        sut.getUnassignedShardInfo();

        // Then
        Request expectedRequest = createRequestWithJson(DefaultElasticsearchClient.METHOD_GET, ClusterAllocationParams.API_ENDPOINT);
        verify(client).performRequest(expectedRequest);
    }

    @Test
    void getUnassignedShardInfo_successResponse_returnUnassignedShardInfo() throws IOException {
        // Given
        Response response = givenClientRespondsWith(HTTP_STATUS_OK);
        String content = givenResponseHasContent(response);
        UnassignedShardInfo expected = givenContentCanBeMappedToUnassignedShardInfo(content);

        // When
        Optional<UnassignedShardInfo> result = sut.getUnassignedShardInfo();

        // Then
        assertThat(result, isPresentAnd(equalTo(expected)));
    }

    @Test
    void getUnassignedShardInfo_responseMapperThrowsException_returnEmpty() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_OK);
        givenResponseMapperThrowsException();

        // When
        Optional<UnassignedShardInfo> result = sut.getUnassignedShardInfo();

        // Then
        assertThat(result, isEmpty());
    }

    @Test
    void getUnassignedShardInfo_errorResponseWithBadRequest_returnEmpty() throws IOException {
        // Given
        givenClientRespondsWith(HTTP_STATUS_BAD_REQUEST);
        givenResponseMapperThrowsException();

        // When
        Optional<UnassignedShardInfo> result = sut.getUnassignedShardInfo();

        // Then
        assertThat(result, isEmpty());
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    void getUnassignedShardInfo_clientThrowsResponseException_returnErrorConnectionInfo() throws IOException {
        // Given
        givenClientThrowsResponseException(HTTP_STATUS_BAD_REQUEST);

        // When
        Optional<UnassignedShardInfo> result = sut.getUnassignedShardInfo();

        // Then
        assertThat(result, isEmpty());
    }

    private Response givenClientRespondsWith(final int statusCode) throws IOException {
        Response response = mockResponse(statusCode);
        when(client.performRequest(any(Request.class))).thenReturn(response);
        return response;
    }

    private ResponseException givenClientThrowsResponseException(final int statusCode) throws IOException {
        Response response = mockResponse(statusCode);
        ResponseException exception = new ResponseException(response);
        when(client.performRequest(any(Request.class))).thenThrow(exception);
        return exception;
    }

    private SSLHandshakeException givenClientThrowsSSLHandshakeException() throws IOException {
        SSLHandshakeException exception = new SSLHandshakeException("[TEST] SSL handshake problem");
        when(client.performRequest(any(Request.class))).thenThrow(exception);
        return exception;
    }

    private IOException givenClientThrowsIOException() throws IOException {
        IOException exception = new IOException("[TEST] some other problem");
        when(client.performRequest(any(Request.class))).thenThrow(exception);
        return exception;
    }

    private Response mockResponse(final int statusCode) {
        Response response = mock(Response.class);
        when(response.getRequestLine()).thenReturn(FAKE_REQUEST_LINE);
        StatusLine statusLine = new BasicStatusLine(HTTP_PROTOCOL_VERSION, statusCode, Randoms.generateString("[TEST] Reason: "));
        when(response.getStatusLine()).thenReturn(statusLine);
        return response;
    }

    private String givenResponseHasContent(final Response response) throws IOException {
        String responseContent = Randoms.generateString("content-");
        when(responseMapper.getContentAsString(response)).thenReturn(responseContent);
        return responseContent;
    }

    private ClusterSettings givenContentCanBeMappedToClusterSettings(final String responseContent) {
        ClusterSettings settings = ClusterSettingsUtils.random();
        when(infoMapper.mapClusterSettings(responseContent)).thenReturn(settings);
        return settings;
    }

    private ClusterInfo givenContentCanBeMappedToClusterInfo(final String responseContent) {
        ClusterInfo info = ClusterInfos.random();
        when(infoMapper.mapClusterInfo(responseContent, responseContent, responseContent)).thenReturn(info);
        return info;
    }

    private List<NodeInfo> givenContentCanBeMappedToNodeInfo(final String responseContent) {
        List<NodeInfo> infos = ClusterNodeInfos.random().getNodeInfos();
        when(infoMapper.mapNodeInfo(responseContent, responseContent, responseContent)).thenReturn(infos);
        return infos;
    }

    private UnassignedShardInfo givenContentCanBeMappedToUnassignedShardInfo(final String responseContent) {
        UnassignedShardInfo info = UnassignedShardInfos.random();
        when(infoMapper.mapUnassignedShardInfo(responseContent)).thenReturn(info);
        return info;
    }

    private void givenResponseMapperThrowsException() throws IOException {
        when(responseMapper.getContentAsString(any(Response.class))).thenThrow(IOException.class);
        when(responseMapper.toMap(any(Response.class))).thenThrow(IOException.class);
        when(responseMapper.toMaps(any(Response.class))).thenThrow(IOException.class);
    }

    private Request createRequestWithJson(final String method, final String endpoint) {
        Request request = new Request(method, endpoint);

        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader(DefaultElasticsearchClient.HEADER_ACCEPT, DefaultElasticsearchClient.CONTENT_TYPE_APPLICATION_JSON);
        request.setOptions(builder);
        request.addParameter(GeneralParams.PARAM_FORMAT, "json");

        return request;
    }
}