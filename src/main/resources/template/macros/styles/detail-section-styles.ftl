<#macro style>
    <style>
        .detail-section {
            width: max-content; /* To nicely wrap the content if it is smaller than the parent's max-width. */
            max-width: 100%; /* To set a maximum if max-content is bigger than the parent's max-width. */
            margin-bottom: 2%;
        }

        .bordered-detail-section {
            width: max-content;
            max-width: 100%;
            margin-bottom: 2%;
            padding: 1em;
            background: white;
            border: solid 1px darkgray;
            border-radius: 1em;
            box-shadow: 0.5em 0.5em 1em rgba(0, 0, 0, 0.1);
        }

        .centered-detail-section {
            margin-left: auto !important;
            margin-right: auto !important;
        }

        /*
        * Reduces the top margin of the first heading in a bordered section.
        */
        .bordered-detail-section > h1:first-child,
        .bordered-detail-section > h2:first-child,
        .bordered-detail-section > h3:first-child,
        .bordered-detail-section > h4:first-child,
        .bordered-detail-section > h5:first-child {
            margin-top: 1%;
        }
    </style>
</#macro>