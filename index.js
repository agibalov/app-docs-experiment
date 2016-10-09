var system = require('system');
var page = require('webpage').create();

// 'file:///home/loki2302/phantomjs-experiment/html5/app.html'
var source = system.args[1];

// '1.pdf'
var destination = system.args[2];

console.log(JSON.stringify({
	source: source,
	destination: destination
}));

page.paperSize = {
	format: 'A4', 
	orientation: 'portrait', 
	margin: {
		top: '1.5cm',
		bottom: '1.0cm',
		left: '1.5cm',
		right: '1.0cm'
	}
};

page.open(source, function(status) {
	if(status !== 'success') {
		console.log('Unable to load the address!');
		phantom.exit(1);
		return;
	}

	window.setTimeout(function() {
		page.evaluate(function(zoom) {
			return document.querySelector('body').style.zoom = zoom;
		}, 0.75);

		page.render(destination);
		phantom.exit();
	}, 1000);
});
