$(function() {
  $("#search-service-providers").on("keyup", function(event) {
    var valueToSearch = $(this).val().toLowerCase();
    $.each($("#search-results tr"), function(index, value) {
      var $tr = $(value);
      if(valueToSearch.length > 0 && $tr.data("sp-name").trim().toLowerCase().indexOf(valueToSearch) >= 0) {
        $tr.removeClass("hidden");
      } else {
        $tr.addClass("hidden");
      }
    });
  });

  $("#search-results tr").on("click", function() {
    $(this).find("form").submit();
  });
});
