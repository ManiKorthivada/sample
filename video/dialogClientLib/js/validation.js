   (function($, window, document) {

    /* Adapting window object to foundation-registry */
    var registry = $(window).adaptTo("foundation-registry");

    /*Validator for TextField - Any Custom logic can go inside validate function - starts */
    registry.register("foundation.validation.validator", {

        selector: "[data-validation=video-validate]",
        validate: function(el) {
            var element = $(el);
            var pattern = "video";
            var value = element.val();
            if (value.length == 0) {

                return "Please Select the field";
            } else {

                patterns = {
                    video:/^.*\.(avi|AVI|wmv|WMV|flv|FLV|mpg|MPG|mp4|MP4|mp3|MP3|ogg|OGG|MPEG|mpeg|MOV|mov)$/

                }

                /*
                 * Test pattern if set. Pattern can be a preset regex pattern name or
                 * a regular expression such as "^\\d+$".
                 */


                    if (!patterns[pattern].test(value)) {
                        return "The field must match the pattern: " + pattern;
                    }
                  

            }

        }
    });
})
($, window, document);