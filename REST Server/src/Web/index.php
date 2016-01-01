		<script src="lib/js/jquery-1.10.2.min.js"></script>
		<script src="js/emulator_script.js"></script>
		<style>
			.content{
				font-family: verdana, arial, tahoma, serif;
				font-size:12px;
			}
			.iPhone{
				margin: 0px 8px 0px 0px;
				padding: 88px 0px 0px 0px;
				width: 502px;
				height: 780px;
				background: url(img/device-iPhone.png) no-repeat top left;
				clear:both;
			}
			.iPhone-inside{
				width: 229px;
				margin-top: -14px;
				margin-left: 47px;
				background: #fff;
				height: 352px;
				overflow: hidden;
			}
			#iframe{
				-moz-transform: scale(0.678);
				-moz-transform-origin: 0 0;
				-o-transform: scale(0.678);
				-o-transform-origin: 0 0;
				-webkit-transform: scale(0.678);
				-webkit-transform-origin: 0 0;
			}
			#loader{
				position: absolute;
				width: 150px;
				text-align: center;
				top: 256px;
				border: 1px solid #666;
				background: #fff;
				padding: 15px;
				left: 78px;
				z-index: 2;
			}
			.tips{color:#666;}
			.fade{
					height: 352px;
					width: 219px;
					background: #fff;
					position: absolute;
					z-index: 1;
					opacity: 0.25;
			}
		</style>
<div class="content">
<form method="get" action="" onsubmit="javascript: return show_loader()">
	<input type="hidden" name="agent" value="1" />
	URL:<br/>
	<input type="text" name="url">
	<input type="submit" value="Emulate"><br/>
	<span class="tips">(e.g. php-drops.blogspot.com)</span>
	<hr/>
</form>
<?php
ERROR_REPORTING(E_ALL);
$url = "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
$tmp = explode("?",$url);
$url = $tmp[0];

define("PROXY_URL", $url);
define("USER_AGENT_IPHONE_IOS5"  , "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3");
define("USER_AGENT_IPAD_IOS5"    , "Mozilla/5.0 (iPad; CPU OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3");
define("USER_AGENT_NEXUS_7"      , "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
define("USER_AGENT_GALAXY_NEXUS" , "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
define("USER_AGENT_BLACK_BERRY10", "Mozilla/5.0 (BB10; Touch) AppleWebKit/537.1+ (KHTML, like Gecko) Version/10.0.0.1337 Mobile Safari/537.1+");

if (isset($_GET['url']))
{
	if(!(substr($_GET['url'],0,7)=="http://" ||  substr($_GET['url'],0,8)=="https://"))	{
		$_GET['url'] = "http://".$_GET['url'];
	}
	$url = htmlspecialchars($_GET['url'], ENT_COMPAT, 'UTF-8');
	if (!isset($_GET['agent']))
	{
		$_GET['agent'] = 1;
	}
	switch($_GET['agent'])
	{
		case 1:
			$user_agent = USER_AGENT_IPHONE_IOS5;
			break;
		case 2:
			$user_agent = USER_AGENT_IPAD_IOS5;
			break;
		case 3:
			$user_agent = USER_AGENT_NEXUS_7;
			break;
		case 4:
			$user_agent = USER_AGENT_GALAXY_NEXUS;
			break;
		case 5:
			$user_agent = USER_AGENT_BLACK_BERRY10;
			break;
		default:
			$user_agent = USER_AGENT_IPAD_IOS5;
			break;
	}
	$proxy = PROXY_URL .'?agent='. $_GET['agent'];
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_VERBOSE, true);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_USERAGENT, $user_agent);
	curl_setopt($ch, CURLOPT_URL, htmlspecialchars_decode($url));
	$result=curl_exec($ch);
	curl_close ($ch);
	$url = $_GET['url'];
	if(strstr($result,'The document has moved')){
		$a_url = preg_match('/<a href="(.+)">/', strtolower($result), $match);
		$url = html_entity_decode(urldecode($match[1]));
	}
}
?>

	<div class="iPhone">
		<div class="iPhone-inside">
			<?php if(isset($_GET['url']) && $url!=""){ ?><div id="loader">loading...</div><?php } ?>
			<div class="fade" id="fade"></div>
			<?php if(isset($_GET['url']) && $url!=""){ ?><iframe onload='parent.hide_loader()' id="iframe" width="340" height="520" src="<?php echo $url; ?>" frameborder="0"></iframe><?php } ?>
		</div>
	</div>
