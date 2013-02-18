//function Magnet() {
    var searchUri = '/search'

    function init() {
        document.querySelector('form').addEventListener('submit', function (e) { e.preventDefault(); query(); });
    }

    function query() {
        document.querySelector('form button').disabled = true;
        //showLoadingIndicator(true);
        var query = document.querySelector('[name=query]').value;
        var runQueryUri = searchUri + '?query=' + query
        var queryReq = new XMLHttpRequest();
        queryReq.open('GET', runQueryUri);
        queryReq.onload = function (e) {
            if (this.status == 200) {
                document.querySelector('.results').appendChild(document.createElement('ul'));
                var results = JSON.parse(this.response);
                for (var i = 0; i < results.length; i++) {
                    addResult(results[i]);
                }
            }
            //showLoadingIndicator(false);
            document.querySelector('form button').disabled = false;
        }
        queryReq.send();
    }

    function addResult(result) {
        var resultItem = document.createElement('li');
        resultItem.innerHTML += '<h2><a href="' + result.uri + '"/>' + result.headline + '</a></h2>';
        if (result.thumbnailUri) {
            resultItem.innerHTML += '<figure><img src = "' + result.thumbnailUri + '"/></figure>';
        }
        document.querySelector('.results ul').appendChild(resultItem);
    }

    init();
//}