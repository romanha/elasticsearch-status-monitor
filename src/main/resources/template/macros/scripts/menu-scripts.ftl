<#macro script>
    <script>
		function hideAllMainSectionsExcept(displayedSectionId) {
			let sections = document.getElementsByClassName("main-section");
			for (let section of sections) {
				if (section.id === displayedSectionId) {
					section.classList.remove("hidden");
				} else {
					section.classList.add("hidden");
				}
			}
		}
    </script>
</#macro>