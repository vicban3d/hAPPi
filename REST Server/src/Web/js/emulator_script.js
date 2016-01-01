function hide_loader(){
	jQuery('#loader').hide();
	jQuery('#fade').removeClass('fade');
}

function show_loader(){
	jQuery('#loader').show();
	jQuery('#fade').addClass('fade');
	return true;
}

