(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".cq-dialog-dropdown-showhide", e.target));
        var selection = $(".coral-Select-button-text")[0];
       if(selection){
			var selectedVal = selection.innerText;
           if(selectedVal=="External"){

               $('[name="./videoPath"]').val("/content/dam/dgtl-content/components/videos");
           }
           if(selectedVal=="Upload"){
               $('[name="./videoPath"]').val("");
               $('[name="./videoid"]').val("");

           }
       }
    });

    $(document).on("selected", ".cq-dialog-dropdown-showhide", function (e) {
        showHideHandler($(this));
        var selection = $(".coral-Select-button-text")[0];
       if(selection){
			var selectedVal = selection.innerText;

           if(selectedVal=="External"){
               $(".coral-TabPanel-tab").removeClass('is-invalid');

           }

       }
    });

    function showHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
                   

        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cqDialogDropdownShowhideTarget");
        var $target = $(target);

        if (target) {
            var value;
            if (component.value) {
                value = component.value;
            } else {
                value = component.getValue();
            }

            
            //$target.not(".hide").addClass("hide");

            var $unselected =  $target.not(".hide");

            // make sure all unselected target elements are hidden.

            $unselected.addClass('hide');



            var $selected = $target.filter("[data-showhidetargetvalue='" + value + "']");
            $('input', $selected).attr('disabled', false);

            // unhide the target element that contains the selected value as data-showhidetargetvalue attribute
            $selected.removeClass("hide");

            $('input', $target.filter('.hide')).attr('disabled', true);
        }
    }

})(document, Granite.$);