var timer = null;

function addnotification(message, type, timeout) {
	removeNotification();

	var newNotification = document.createElement("div");
	newNotification.id = "notification";
	dojo.byId("notifications_container").appendChild(newNotification);

	var table = document.createElement('table');
	var tBodyElem = document.createElement("tbody");
	var tableRow = document.createElement('tr');
	var tableTD = document.createElement('td');

	var newNotificationImg = document.createElement('div');
	newNotification.appendChild(newNotificationImg);

	tableTD.appendChild(newNotificationImg);
	tableRow.appendChild(tableTD);
	tableTD = document.createElement('td');
	dojo.attr(tableTD, 'valign', 'middle');
	dojo.attr(tableTD, 'style', 'padding-left:3px;');

	var newNotificationMsg = document.createElement('span');
	newNotificationMsg.innerHTML = message;

	tableTD.appendChild(newNotificationMsg);
	tableRow.appendChild(tableTD);
	tBodyElem.appendChild(tableRow);
	table.appendChild(tBodyElem);

	newNotification.appendChild(table);
	switch (type) {
	case "error":
		dojo.toggleClass(newNotification, "error");
		dojo.toggleClass(newNotificationImg, "error_img");
		break;
	case "info":
		dojo.toggleClass(newNotification, "success");
		dojo.toggleClass(newNotificationImg, "success_img");
		break;
	}

	timer = setTimeout("removeNotification()", timeout);
}

function removeNotification() {
	var notification = document.getElementById("notification");
	if (notification != null) {
		clearTimeout(timer);
		notification.parentNode.removeChild(notification);
	}
}
