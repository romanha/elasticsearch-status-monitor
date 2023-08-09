<#macro style>
    <style>
        .cluster-details-grid-container {
            display: grid;
            grid-template-columns: auto auto auto;
            grid-column-gap: 8em;

            justify-content: center;
            margin-top: 2em;
        }

        /* Display only 2 columns on smaller screens. */
        @media screen and (max-width: 1999px) {
            .node-list-grid-container {
                display: grid;
                grid-template-columns: auto auto;
                grid-column-gap: 2em;
                grid-row-gap: 1em;

                justify-content: center;
            }
        }

        /* Display 3 columns on wider screens. */
        @media screen and (min-width: 2000px) {
            .node-list-grid-container {
                display: grid;
                grid-template-columns: auto auto auto;
                grid-column-gap: 2em;
                grid-row-gap: 1em;

                justify-content: center;
            }
        }

        .about-grid-container {
            display: grid;
            grid-template-columns: auto auto;
            grid-column-gap: 2em;

            justify-content: center;
        }
    </style>
</#macro>