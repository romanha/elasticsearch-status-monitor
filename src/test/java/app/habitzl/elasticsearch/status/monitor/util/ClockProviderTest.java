package app.habitzl.elasticsearch.status.monitor.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ClockProviderTest {

	private ClockProvider sut;

	@BeforeEach
	void setUp() {
		sut = new ClockProvider();
	}

	@Test
	void get_clockNotYetExisting_returnsClock() {
		// Given
		// clock does not exist yet

		// When
		Clock clock = sut.get();

		// Then
		assertThat(clock, notNullValue());
	}

	@Test
	void get_clockAlreadyExists_returnExistingClock() {
		// Given
		Clock existingClock = givenClockAlreadyExists();

		// When
		Clock clock = sut.get();

		// Then
		assertThat(clock, is(existingClock));
	}

	private Clock givenClockAlreadyExists() {
		return sut.get();
	}
}