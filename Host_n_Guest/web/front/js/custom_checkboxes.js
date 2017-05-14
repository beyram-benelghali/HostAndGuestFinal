jQuery(document).ready(function(){

    // Check for input with hardcoded "checked" and update gracefully the pseudo_input
    $('input').each(function() {
        if ($(this).prop("checked") == true) {
            $('.pseudo_input[data-for-id=' + (this.id) + ']').delay(250).addClass('active');
        }
    })

    // Click event
    $('.pseudo_input').click(function() {
        // If radio, uncheck siblings
        if ($(this).hasClass('radio')) {
            $(this).siblings().removeClass('active');
        }
        // Track down and update the related input
        var related_input = $('#' + $(this).attr('data-for-id'));
        related_input.prop(
            "checked", (related_input.prop("checked") == false ? true : false)
        );
        $(this).toggleClass('active');
    });
    // /Click event

});
