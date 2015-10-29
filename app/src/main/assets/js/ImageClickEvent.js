javascript:(function() {
    var images = document.getElementsByTagName('img');
    for(var i = 0; i < images.length; i++) {
        images[i].onclick = function() {
            {platform}.openImage(this.src)
        }
    }
})();