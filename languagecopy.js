;(function ($, ns, channel) {
    $(window).load(function () {
        var windowUI = $(window).adaptTo("foundation-ui");

        var $container = channel.find("#language-copy-container");

        var xfPath = $container.attr("data-page-path").trim();
        if (!xfPath) {
            throw new Error("No 'path' attribute provided to workflow'.");
        }

        var $lan = $container.find(".variation-lan");
        var $device = $container.find(".variation-device");
        var $path = $container.find(".variation-path");
        alertHide();

        $.ajax({
            type: "GET",
            url: "/content/list-variation.json",
            data: {
            	path: $path.val()
            	},
            dataType: "json",
            contentType: 'application/json',
            success: function(data)
            			{
            			    drawTable(data);
            			    console.log(data);
			            },
            error: function(response)
			            {
            	if (response.status == 409)
            		{
            			$(".coral-combination-error").show();
            		}
            	else
            		{
	            		$(".coral-error").show();
            		}
			            }
          });


    $container.on("change", ".variation-device, .variation-lan", function() {
        alertHide();
    });



    $container.on("click", ".language-button", function() {
    	alertHide();

        if ($lan.val() == "en" && $device.val() == "web")
        {
        $(".coral-combination-error").show();
        }
    	else
        {
        $.ajax({
            type: "GET", 
            url: "/content/content-variation.json",
            data: { 
            	language: $lan.val(), 
            	device: $device.val(), 
            	path: $path.val()
            	},
            dataType: "html",
            contentType: 'application/json',
            success: function(data) 
            			{ 
			            	$(".coral-success").show();
			            },
            error: function(response) 
			            { 
            	if (response.status == 409)
            		{
            			$(".coral-combination-error").show();
            		}
            	else
            		{
	            		alertHide();
	            		$(".coral-error").show();
            		}
			            }
          });

        }
	});

        function alertHide(){
            $(".coral-error").hide();
            $(".coral-success").hide();
            $(".coral-combination-error").hide();
        }


function drawTable(data) {
    //$("#variationData").append('<tbody is="coral-table-body ">');

    for (var i = 0; i < data.length; i++) {
        console.log(data[i]);
        drawRow(data[i]);
    }
}

function drawRow(rowData) {
    var row = $('<tr is="coral-table-row" />')
    $("#variationData").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
    row.append($('<td is="coral-table-cell">' + rowData.variationName + '</td>'));
    row.append($('<td is="coral-table-cell"><a href = '+rowData.variationPath+'>'+rowData.variationPath+'</a></td>'));
}

    });
})(jQuery, Granite.author, $(document));