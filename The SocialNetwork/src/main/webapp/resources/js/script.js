$(function() {

  // radiys box
  $('.menu_nav ul li a').css({
    "border-radius" : "8px",
    "-moz-border-radius" : "8px",
    "-webkit-border-radius" : "8px"
  });
  $('.sb_menu li a, .ex_menu li a').css({
    "border-radius" : "15px",
    "-moz-border-radius" : "15px",
    "-webkit-border-radius" : "15px"
  });
  $('.mainbar .spec a').css({
    "border-radius" : "10px",
    "-moz-border-radius" : "10px",
    "-webkit-border-radius" : "10px"
  });
  $('.pagenavi a, .pagenavi .current').css({
    "border-radius" : "5px",
    "-moz-border-radius" : "5px",
    "-webkit-border-radius" : "5px"
  });

});

$(document).ready(function() {
  $('#registeropen').click(function() {
    $('#overlay').show('slow', function() {
      $('#registerfade').fadeIn('slow');
    });
  });
  
  $('#loginopen').click(function() {
    $('#overlay').show('slow', function() {
      $('#loginfade').fadeIn('slow');
    });
  });

  $('.registerclose').click(function() {
    $('#registerfade').hide('slow', function() {
      $('#overlay').fadeOut();
    });
  });
  
  $('.loginclose').click(function() {
    $('#loginfade').hide('slow', function() {
      $('#overlay').fadeOut();
    });
  });

  $('#overlay').click(function() {
    $('#registerfade').hide('slow', function() {
      $('#overlay').fadeOut();
    });
    $('#loginfade').hide('slow', function() {
      $('#overlay').fadeOut();
    });
  });
});