<#macro style>
    <style>
        body {
            font-family: Calibri, sans-serif;
            max-width: 100%;
            margin: 2% 8%;
            background-color: #f4f3f1;
        }

        /*
        * All the main div elements should have the max-width of 80%.
        */
        body > div {
            max-width: 80% !important;
        }

        .error {
            font-weight: bold;
            color: red;
        }

        .warn {
            font-weight: bold;
            color: orange;
        }

        .success {
            font-weight: bold;
            color: green;
        }

        .bold {
            font-weight: bold;
        }

        .centered {
            text-align: center;
        }

        .keep-new-lines {
            white-space: pre-line;
        }
    </style>
</#macro>