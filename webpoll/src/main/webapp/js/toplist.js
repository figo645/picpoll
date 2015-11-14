(function() {
  var all_img_url = "stub_data.json";
  $.getJSON(all_img_url, function(data) {
    console.log(data);
    console.log(data.data);
    var img_lst = data.data;
    $.each(img_lst, function(pic) {
      console.log(pic.idpic + " -> " + pic.poll + "_" + pic.username + "_" + pic.picUrl);
    });
  });
})();