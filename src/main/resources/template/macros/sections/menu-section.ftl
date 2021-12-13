<#macro section>
    <nav class="slidemenu">

        <input type="radio"
               name="slideItem"
               id="menu-item-analysis"
               class="slide-toggle"
               onclick="hideAllMainSectionsExcept('analysis-section')"
               checked/>
        <label for="menu-item-analysis">
            <p class="icon">&#128203;</p>
            <span>Analysis</span>
        </label>

        <input type="radio"
               name="slideItem"
               id="menu-item-cluster"
               class="slide-toggle"
               onclick="hideAllMainSectionsExcept('cluster-section')"/>
        <label for="menu-item-cluster">
            <p class="icon">&#127760;</p>
            <span>Cluster</span>
        </label>

        <input type="radio"
               name="slideItem"
               id="menu-item-nodes"
               class="slide-toggle"
               onclick="hideAllMainSectionsExcept('nodes-section')"/>
        <label for="menu-item-nodes">
            <p class="icon">&#128187;</p>
            <span>Nodes</span>
        </label>

        <input type="radio"
               name="slideItem"
               id="menu-item-indices"
               class="slide-toggle"
               onclick="hideAllMainSectionsExcept('indices-section')"/>
        <label for="menu-item-indices">
            <p class="icon">&#128194;</p>
            <span>Indices</span>
        </label>

        <input type="radio"
               name="slideItem"
               id="menu-item-about"
               class="slide-toggle"
               onclick="hideAllMainSectionsExcept('about-section')"/>
        <label for="menu-item-about">
            <p class="icon">&#10068;</p>
            <span>About</span>
        </label>

        <div class="clear"></div>

        <div class="slider">
            <div class="bar"></div>
        </div>
    </nav>
</#macro>