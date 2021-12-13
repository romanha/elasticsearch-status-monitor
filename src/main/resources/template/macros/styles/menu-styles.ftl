<#assign numberOfMenuItems = 5>
<#assign spacePerMenuItemInPixel = 150>
<#assign menuItemWidthInPixel = 50>
<#assign menuItemWidthInPercent = 100/numberOfMenuItems>
<#macro style>
    <style>
        nav * {
            margin: 0;
            padding: 0;
        }

        .clear {
            clear: both;
        }

        .slide-toggle {
            display: none;
        }

        .slidemenu {
            font-family: Calibri, sans-serif;
            max-width: ${numberOfMenuItems * spacePerMenuItemInPixel}px;
            margin: 3em auto;
            overflow: hidden;
        }

        .slidemenu label {
            width: ${menuItemWidthInPercent}%;
            text-align: center;
            display: block;
            float: left;
            color: #333;
            opacity: 0.5;

        }

        .slidemenu label:hover {
            cursor: pointer;
            color: black;
            opacity: 1;
        }

        .slidemenu label span {
            display: block;
            padding: 10px;
        }

        .slidemenu label .icon {
            font-size: 20px;
            border: solid 2px #333;
            text-align: center;
            height: 50px;
            width: ${menuItemWidthInPixel}px;
            display: block;
            margin: 0 auto;
            line-height: 50px;
            border-radius: 50%;
        }

        /* Bar Style */
        .slider {
            width: 100%;
            height: 5px;
            display: block;
            background: #ccc;
            margin-top: 10px;
            border-radius: 5px;
        }

        .slider .bar {
            width: ${menuItemWidthInPercent}%;
            height: 5px;
            background: #333;
            border-radius: 5px;
        }

        /* Animations */
        .slidemenu label, .slider .bar {
            transition: all 500ms ease-in-out;
            -webkit-transition: all 500ms ease-in-out;
            -moz-transition: all 500ms ease-in-out;
        }

        /* Toggle */
        .slidemenu .slide-toggle:checked + label {
            opacity: 1;
        }

        .slidemenu #menu-item-analysis:checked ~ .slider .bar {
            margin-left: 0;
        }

        .slidemenu #menu-item-cluster:checked ~ .slider .bar {
            margin-left: ${menuItemWidthInPercent * 1}%;
        }

        .slidemenu #menu-item-nodes:checked ~ .slider .bar {
            margin-left: ${menuItemWidthInPercent * 2}%;
        }

        .slidemenu #menu-item-indices:checked ~ .slider .bar {
            margin-left: ${menuItemWidthInPercent * 3}%;
        }

        .slidemenu #menu-item-about:checked ~ .slider .bar {
            margin-left: ${menuItemWidthInPercent * 4}%;
        }
    </style>
</#macro>