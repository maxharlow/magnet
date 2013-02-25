function Magnet() {
    var searchUri = '/search';

    function init() {
        document.querySelector('form').addEventListener('submit', function (e) { e.preventDefault(); query(); });
    }

    function query() {
        document.querySelector('form button').disabled = true;
        var query = document.querySelector('[name=query]').value;
        var runQueryUri = searchUri + '?query=' + query;
        var queryReq = new XMLHttpRequest();
        queryReq.open('GET', runQueryUri);
        queryReq.onload = function (e) {
            if (this.status == 200) {
                document.querySelector('.results').innerHTML = '';
                var results = JSON.parse(this.response);
                for (var i = 0; i < results.length; i++) {
                    addResult(results[i]);
                }
            }
            document.querySelector('form button').disabled = false;
        }
        queryReq.send();
    }

    function addResult(result) {
        var resultItem = document.createElement('li');
        var html = '<a href="' + result.uri + '">';
        if (result.thumbnailUri) {
            html += '<img src = "' + result.thumbnailUri + '"/>';
        }
        html += '<h2>' + result.headline + '</h2>';
        for (var i = 0; i < result.thingUris.length; i++) {
            html += '<span>' + result.thingUris[i].name + '</span>';
        }
        html += '</a>';
        if (result.thingUris.length == 0) {
            return; //smoke and mirrors
        }
        resultItem.innerHTML = html;
        document.querySelector('.results').appendChild(resultItem);
    }

    init();
}

document.addEventListener('DOMContentLoaded', new Magnet());