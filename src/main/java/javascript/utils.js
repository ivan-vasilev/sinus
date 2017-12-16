function changeFilterItemVisibility(itemId, imageId) {
	var itm = document.getElementById(itemId);
	var image = document.getElementById(imageId);
	if (itm != null) {
		if (itm.style.display == '') {
			if (image != null) {image.src = '/images/right.png'; image.style.margin = '7px 0px 0px 10px';}
			 dojo.fx.wipeOut({
                 node: itemId,
                 duration:200
             }).play();
		} else {
			if (image != null) {image.src = '/images/down.png'; image.style.margin = '8px 0px 0px 10px';}
			dojo.fx.wipeIn({
                node: itemId,
                duration:200
            }).play();
		}
	}
}

function initCheckBoxes(containerId) {
	dojo.query('#' + containerId + ' input[type=checkbox]').forEach(function(node, index, arr) {
		createChb(node);
	});
}

/**
 * Syzdawa custom checkbox
 * @param chbElement checkbox
 */
function createChb(chbElement) {
	
	var newOuterDiv = chbElement.parentNode;

    if (chbElement.checked) {
    	dojo.addClass(newOuterDiv, 'chbox_outer_div chbox_outer_div_ch');
    } 
    
    chbElement.onmouseover = function() {
    	dojo.removeClass(newOuterDiv);
    	if (this.checked) {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div chbox_outer_div_ch_h');
    	} else {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div chbox_outer_div_h');
    	}
    	
    };
    
    chbElement.onmouseout = function() {
    	dojo.removeClass(newOuterDiv);
    	if (this.checked) {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div chbox_outer_div_ch');
    	} else {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div');
    	}
    };
    
    var oldOnClick = chbElement.onclick;
    
    chbElement.onclick = function(event) {
    	dojo.removeClass(newOuterDiv);
    	if (this.checked) {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div chbox_outer_div_ch');
    	} else {
    		dojo.addClass(newOuterDiv, 'chbox_outer_div');
    	}
    	
    	if (oldOnClick != null) {
    		oldOnClick(event);
    	}

    };
}

function changeTitlePaneVisibility(paneTitleNode) {
	if (paneTitleNode != null) {
		var parent = paneTitleNode.parentNode;
		var container = dojo.query('> div.titlepane_c', parent)[0];
		if (container.style.display == '') {
			dojo.query('img.titlepane_hi', paneTitleNode)[0].style.backgroundPosition = '-14px 0';
			dojo.fx.wipeOut({
                node: container
            }).play();
		} else {
			dojo.query('img.titlepane_hi', paneTitleNode)[0].style.backgroundPosition = '0 0';
			dojo.fx.wipeIn({
                node: container
            }).play();
		}
		
	}	
}

function showLightBox(elemObj) {
	if (dojo.hasAttr(elemObj, 'imgLink')) {
		dijit.byId('dojoxLightboxDialog').show({group:'part_pictures', title: dojo.attr(elemObj, 'imgTitle'), href: dojo.attr(elemObj, 'imgLink')});
	}
}