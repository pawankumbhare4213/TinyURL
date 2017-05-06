<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>	
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="PRK's URL Shortner">
	<meta name="keywords"
		content="pawan kumbhare,pawan,kumbhare,r kumbhare,ravishankar kumbhare,profile,portfolio,prk,resume,cv,curriculum vitae,
				personal,professional,tiny,url,tiny url,url shortner,shortner,url details,tu">
	<meta name="author" content="Pawan Kumbhare">
	<meta name="theme-color" content="#FFA500">
	<link rel="icon" type="image/png" href="http://www.pawankumbhare.com/static/images/PRK-gray.png" />
	<title>PRK - URL Shortner</title>
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
		integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
		integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
		integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<body>
	<div class="g-recaptcha" data-sitekey="6LfsHSAUAAAAANNJWQAn0TU3iYkbLCCztxSy_EGr" data-callback="setURL" data-size="invisible"></div>
	<nav class="navbar navbar-default" style="margin-bottom: 0px;">
		<div class="container">
			<div class="navbar-header"> 
				<a class="navbar-brand" target="_blank" href="http://www.pawankumbhare.com"> 
					<img alt="PRK" src="http://www.pawankumbhare.com/static/images/PRK-gray.png" style="margin-top: -2px; width: 60px;">  
				</a>
				<a class="navbar-brand" href="/"> URL Shortner </a>
			</div>
		</div>
	</nav>
	<div style="background-color: orange;">
		<div class="container">
			<div class="page-header" style="border-bottom: 0px;">
				<p class="h1" style="color: whitesmoke; margin-top: 0px;">Simplify your links</p>
				<div class="input-group">
					<input id="long-url" type="text" class="form-control" placeholder="http://  Place your original URL here..."> 
						<span class="input-group-btn">
						<button id="shorten-url" class="btn btn-default disabled" type="button" disabled="disabled"
								onclick="javascript:verifyAndSetURL();">Shorten URL</button>
					</span>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="error-section" class="alert alert-danger" style="display: none; margin-bottom: 20px; margin-top: 20px;"></div>
		<div id="info-section" class="alert alert-success" style="display: none; margin-bottom: 20px; margin-top: 20px;"></div>			
	</div>
	<div style="background-color: #EEEEEE;">
		<div class="container">
			<div class="page-header" style="border-bottom: 0px;">
				<p class="h1" style="margin-top: 0px;">Click analytics</p>
				<p class="h4 lead" style="color: graytext;">All links are public and can be accessed by anyone.</p>
				<div class="input-group">
					<input id="short-url" type="text" class="form-control" placeholder="Place your short URL here..."> 
					<span class="input-group-btn">
						<button id="get-details" class="btn btn-default disabled" disabled="disabled" type="button" 
								onclick="javascript:getURLDetails();">Get Details</button>
					</span>
				</div>
				<div id="url-details" style="display: none; padding-top: 30px;">
					<div class="panel panel-primary" style="margin-bottom: 0px;">
						<div class="panel-heading">URL Details</div>
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>Short URL</th>
										<th>Long URL</th>
										<th>Date Created</th>
										<th>Total Hits</th>
									</tr>
								</thead>
								<tbody id="rows"></tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#long-url').keyup(checkButtons);
			$('#long-url').change(checkButtons);
			$('#short-url').keyup(checkButtons);
			$('#short-url').change(checkButtons);
			$('#long-url').keypress(function(e) {
			    if(e.which == 13 && !$('#shorten-url').hasClass('disabled')) setURL();
			});
			$('#short-url').keypress(function(e) {
			    if(e.which == 13 && !$('#get-details').hasClass('disabled')) getURLDetails();
			});
		});
		
		function checkButtons() {
			var longURL = $('#long-url').val().trim();
			if (!longURL) {
				$('#shorten-url').addClass('disabled');
				$('#shorten-url').prop('disabled', true);
			} else {
				$('#shorten-url').removeClass('disabled');
				$('#shorten-url').prop('disabled', false);
			}
			var shortURL = $('#short-url').val().trim();
			if (!shortURL) {
				$('#get-details').addClass('disabled');
				$('#get-details').prop('disabled', true);
			} else {
				$('#get-details').removeClass('disabled');
				$('#get-details').prop('disabled', false);
			}
		}
	
		function verifyAndSetURL() {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			var longURL = $('#long-url').val().trim();
			if (!longURL) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Please enter your original URL.');
				return;
			}
			if(longURL.startsWith('http://') || longURL.startsWith('https://')) grecaptcha.execute();
			else $('#error-section').css('display', '').html("<strong>Oops!</strong> Don't forget to include the http/https protocol in your URL.");
		}
		
		function setURL(token) {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			grecaptcha.reset();
			if (!token) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Something went wrong, please try again.');
			}
			var longURL = $('#long-url').val().trim();
			if (!longURL) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Please enter your original URL.');
				return;
			}
			$.ajax({
				url : "/",
				data : {
					url : longURL,
					captcha : token
				},
				type : 'POST'
			}).done(function(data) {
				if (data.SHORT_URL) {
					var relURL = data.SHORT_URL.substr(data.SHORT_URL.lastIndexOf('/'));
					$('#info-section').css('display', '').html('<strong>Success!</strong> Your short URL is <a target="_blank" href="' 
							+ relURL + '">' + data.SHORT_URL + '</a>. It will expire in 30 days.');
					$('#long-url').val('');
				} else if(data.MESSAGE) {
					$('#error-section').css('display', '').html('<strong>Oops!</strong> ' + data.MESSAGE);
				}
				checkButtons();
			}).fail(function() {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Something went wrong, please try again.');
			});
		}

		function getURLDetails() {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			$('#url-details').css('display', 'none');
			var shortURL = $('#short-url').val().trim().replace("http://", "").replace("https://", "");
			if (!shortURL) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Please enter your short URL.');
				return;
			}
			var shortCode = shortURL.substr(shortURL.lastIndexOf('/') + 1);
			var reURL = window.location.hostname + '/' + shortCode;
			if (reURL !== shortURL) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Invalid URL, please try again.');
				return;
			}
			$.ajax({
				url : '/details/' + shortCode,
				type : 'GET'
			}).done(function(data) {
				if (data.LONG_URL) {
					var relURL = data.SHORT_URL.substr(data.SHORT_URL.lastIndexOf('/'));
					var html = '<tr><td><a target="_blank" href="' + relURL + '">' + data.SHORT_URL + '</a></td>'
						+ '<td>' + data.LONG_URL + '</td><td>' + new Date(data.TIMESTAMP).toLocaleString() + '</td><td>' + data.HIT_COUNT + '</td></tr>';
					$('#rows').html(html);
					$('#url-details').css('display', '');
				} else if(data.MESSAGE) {
					$('#error-section').css('display', '').html('<strong>Oops!</strong> ' + data.MESSAGE);
				}
				checkButtons();
			}).fail(function() {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Something went wrong, please try again.');
			});
		}
	</script>
</body>
</html>