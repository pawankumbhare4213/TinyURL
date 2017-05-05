<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
		integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
		integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
		integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<title>PRK - Tiny URL</title>
</head>
<body>
	<div class="g-recaptcha" data-sitekey="6LfsHSAUAAAAANNJWQAn0TU3iYkbLCCztxSy_EGr" data-callback="setURL" data-size="invisible"></div>
	<div class="container">
		<div class="page-header">
		  	<h1>
		  		<a target="_blank" href="http://www.pawankumbhare.com" style="cursor: hand; text-decoration: none; color: black;">PRK</a>
		  		<small>&nbsp;&nbsp;&nbsp;URL Shortner</small>
		  	</h1>
		</div>
	</div>
	<div class="jumbotron" style="background-color: orange; margin-bottom: 0px;">
		<div class="container">
			<p class="h1" style="color: whitesmoke; margin-top: 0px;">Simplify your links</p>
			<div class="input-group">
				<input id="long-url" type="text" class="form-control" placeholder="Place your original URL here..."> 
					<span class="input-group-btn">
					<button id="shorten-url" class="btn btn-default disabled" type="button" onclick="javascript:verifyAndSetURL();">Shorten URL</button>
				</span>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="error-section" class="alert alert-danger" style="display: none; margin-bottom: 20px; margin-top: 20px;"></div>
		<div id="info-section" class="alert alert-success" style="display: none; margin-bottom: 20px; margin-top: 20px;"></div>
	</div>
	<div class="jumbotron" style="margin-bottom: 0px;">
		<div class="container">
			<p class="h1" style="margin-top: 0px;">Click analytics</p>
			<p class="lead" style="color: graytext;">All links are public and can be accessed by anyone.</p>
			<div class="input-group">
				<input id="short-url" type="text" class="form-control" placeholder="Place your short URL here..."> 
				<span class="input-group-btn">
					<button id="get-details" class="btn btn-default disabled" type="button" onclick="javascript:getURLDetails();">Get Details</button>
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
			} else {
				$('#shorten-url').removeClass('disabled');
			}
			var shortURL = $('#short-url').val().trim();
			if (!shortURL) {
				$('#get-details').addClass('disabled');
			} else {
				$('#get-details').removeClass('disabled');
			}
		}
	
		function verifyAndSetURL() {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			grecaptcha.execute();
		}
		
		function setURL(token) {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			if (!token) {
				grecaptcha.reset();
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
				grecaptcha.reset();
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Something went wrong, please try again.');
			});
		}

		function getURLDetails() {
			$('#info-section').css('display', 'none');
			$('#error-section').css('display', 'none');
			$('#url-details').css('display', 'none');
			var shortURL = $('#short-url').val().trim();
			if (!shortURL) {
				$('#error-section').css('display', '').html('<strong>Oops!</strong> Please enter your short URL.');
				return;
			}
			shortURL = shortURL.substr(shortURL.lastIndexOf('/') + 1);
			$.ajax({
				url : '/details/' + shortURL,
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