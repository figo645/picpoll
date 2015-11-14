var m_picpoll = {
  vote: function(img_id, poller) {
    $.ajax({
      url: "http://127.0.0.1:8080/webpoll/vote",
      dataType: "json",
      method: "POST",
      data: {
        "idpic": img_id,
        "username": poller
      }
    }).done(function(done_res){
      $("<div>").addClass("alert alert-success").attr({"role":"alert"}).appendTo($("#main_panel"));
    }).fail(function(data){
      $("<div>").addClass("alert alert-danger").attr({"role":"alert"}).appendTo($("#main_panel"));
    });
  },
  display: function(){ console.log(this); },
  retrieveAll: function() {  
    var all_img_url = "stub_data.json";
    $.getJSON(all_img_url, function(data) {
      var img_lst = data.data;
      console.log(img_lst);
    });
  }
};

$(document).ready(function(){
  m_picpoll.retrieveAll();
  m_picpoll.display();
  $("#firstPicVoteBtn").click(function(){ m_picpoll.vote("firstPic", "austin"); });
});
