<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>工具出错</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <style>
        html, body {
            padding: 0px;
            margin: 0px;
            width: 100%;
            height: 100%;
        }

        #root {
            display: flex;
            justify-content: center;
            height: 100%;
            flex-direction: column;
        }

        #content {
            align-self: center;
            position: relative;
            top: -100px;
        }

        #bg {
            width: 200px;
            height: 200px;
            background: #ff7676;
            border-radius: 180px;
            box-sizing: border-box;
            padding-top: 33px;
            position: relative;
            box-shadow: inset 0px 0px 10px 1px #d96d6d;
        }

        #wheel-div {
            text-align: center;
        }

        #big-wheel {
            width: 130px;
            height: 130px;
            position: relative;
            left: 0px;
            filter: drop-shadow(0px 0px 2px #a05050);
            -webkit-filter: drop-shadow(0px 0px 2px #a05050);
            -moz-filter: drop-shadow(0px 0px 2px #a05050);
            -o-filter: drop-shadow(0px 0px 2px #a05050);
        }

        #small-wheel {
            width: 40px;
            height: 40px;
            position: relative;
            top: 5px;
            left: -10px;
            filter: drop-shadow(0px 0px 2px #a05050);
            -webkit-filter: drop-shadow(0px 0px 2px #a05050);
            -moz-filter: drop-shadow(0px 0px 2px #a05050);
            -o-filter: drop-shadow(0px 0px 2px #a05050);
        }

        #msg-div {
            text-align: center;
            color: #ff7676;
            font-size: 1.1em;
            margin-top: 20px;
        }

        #reload-div{
            align-self: center;
		    position: absolute;
		    bottom: 80px;
        }
        #reload-div button{
            width: 6.5em;
            height: 2.4em;
            border-radius: 1.3em;
            border: 0px;
            background-color: transparent;
            color: #ff7d3d;
            box-shadow: 0px 0px 1px 1px #ff6a20;
        }

        #reload-div button:active,  #reload-div button:visited,  #reload-div button:focus{
            background-color: #f96;
            color: #fff;
            outline: none;
        }

        #error-msg {
            position: absolute;
            bottom: 0px;
            color: #989898;
            font-size: 0.7em;
            align-self: center;
        }

        @media screen and (max-width: 767px) {

            #content {
                align-self: center;
                position: relative;
                top: -100px;
            }

            #bg {
                width: 140px;
                height: 140px;
                background: #ff7676;
                border-radius: 180px;
                box-sizing: border-box;
                padding-top: 20px;
                position: relative;
                box-shadow: inset 0px 0px 10px 1px #d96d6d;
            }

            #wheel-div {
                text-align: center;
            }

            #big-wheel {
                width: 95px;
                height: 95px;
                position: relative;
                left: 5px;
                top: -5px;
                filter: drop-shadow(0px 0px 2px #a05050);
                -webkit-filter: drop-shadow(0px 0px 2px #a05050);
                -moz-filter: drop-shadow(0px 0px 2px #a05050);
                -o-filter: drop-shadow(0px 0px 2px #a05050);
            }

            #small-wheel {
                width: 30px;
                height: 30px;
                position: relative;
                top: -4px;
                left: -8px;
                filter: drop-shadow(0px 0px 2px #a05050);
                -webkit-filter: drop-shadow(0px 0px 2px #a05050);
                -moz-filter: drop-shadow(0px 0px 2px #a05050);
                -o-filter: drop-shadow(0px 0px 2px #a05050);
            }

            #msg-div {
                text-align: center;
                color: #ff7676;
                font-size: 0.9em;
                margin-top: 30px;
            }
        }

        /*动画*/

        #big-wheel, #wheel-div {
            margin: 0 auto;
            text-align: center;
            padding: 0;

            animation-name: big-wheel;
            animation-duration: 8s;
            animation-timing-function: linear;
            animation-delay: 0s;
            animation-iteration-count: infinite;
            animation-direction: normal;
            animation-play-state: running;

            -moz-animation-name: big-wheel;
            -moz-animation-duration: 8s;
            -moz-animation-timing-function: linear;
            -moz-animation-delay: 0s;
            -moz-animation-iteration-count: infinite;
            -moz-animation-direction: normal;
            -moz-animation-play-state: running;

            -webkit-animation-name: big-wheel;
            -webkit-animation-duration: 8s;
            -webkit-animation-timing-function: linear;
            -webkit-animation-delay: 0s;
            -webkit-animation-iteration-count: infinite;
            -webkit-animation-direction: normal;
            -webkit-animation-play-state: running;

            -o-animation-name: big-wheel;
            -o-animation-duration: 8s;
            -o-animation-timing-function: linear;
            -o-animation-delay: 0s;
            -o-animation-iteration-count: infinite;
            -o-animation-direction: normal;
            -o-animation-play-state: running;
        }

        @keyframes big-wheel {
            from {
                transform: rotate(0deg);
            }
            to {
                transform: rotate(360deg);
            }
        }

        @-moz-keyframes big-wheel {
            from {
                -moz-transform: rotate(0deg);
            }
            to {
                -moz-transform: rotate(360deg);
            }
        }

        @-webkit-keyframes big-wheel {
            from {
                -webkit-transform: rotate(0deg);
            }
            to {
                -webkit-transform: rotate(360deg);
            }
        }

        @-o-keyframes big-wheel {
            from {
                -o-transform: rotate(0deg);
            }
            to {
                -o-transform: rotate(360deg);
            }
        }

        #small-wheel {
            margin: 0 auto;
            text-align: center;
            padding: 0;

            animation-name: small-wheel;
            animation-duration: 3s;
            animation-timing-function: linear;
            animation-delay: 0s;
            animation-iteration-count: infinite;
            animation-direction: normal;
            animation-play-state: running;

            -moz-animation-name: small-wheel;
            -moz-animation-duration: 3s;
            -moz-animation-timing-function: linear;
            -moz-animation-delay: 0s;
            -moz-animation-iteration-count: infinite;
            -moz-animation-direction: normal;
            -moz-animation-play-state: running;

            -webkit-animation-name: small-wheel;
            -webkit-animation-duration: 3s;
            -webkit-animation-timing-function: linear;
            -webkit-animation-delay: 0s;
            -webkit-animation-iteration-count: infinite;
            -webkit-animation-direction: normal;
            -webkit-animation-play-state: running;

            -o-animation-name: small-wheel;
            -o-animation-duration: 3s;
            -o-animation-timing-function: linear;
            -o-animation-delay: 0s;
            -o-animation-iteration-count: infinite;
            -o-animation-direction: normal;
            -o-animation-play-state: running;
        }

        @keyframes small-wheel {
            from {
                transform: rotate(0deg);
            }
            to {
                transform: rotate(-360deg);
            }
        }

        @-moz-keyframes small-wheel {
            from {
                -moz-transform: rotate(0deg);
            }
            to {
                -moz-transform: rotate(-360deg);
            }
        }

        @-webkit-keyframes small-wheel {
            from {
                -webkit-transform: rotate(0deg);
            }
            to {
                -webkit-transform: rotate(-360deg);
            }
        }

        @-o-keyframes small-wheel {
            from {
                -o-transform: rotate(0deg);
            }
            to {
                -o-transform: rotate(-360deg);
            }
        }

    </style>

</head>
<body>
<div id="root">
    <div id="content">
        <div id="bg">
            <div id="wheel-div">
                <svg t="1555469400976" class="img" id="big-wheel" style="" viewBox="0 0 1025 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="1915" xmlns:xlink="http://www.w3.org/1999/xlink"
                     width="80" height="80">
                    <defs>
                        <style type="text/css"></style>
                    </defs>
                    <path d="M970.666793 405.196103l-97.704013-19.476943a385.388052 385.388052 0 0 0-17.241884-41.188947l55.557183-83.016482a63.858832 63.858832 0 0 0-7.982354-80.462128l-61.623772-61.943067a63.858832 63.858832 0 0 0-80.462129-7.982354l-83.016482 55.557184a386.02664 386.02664 0 0 0-41.188946-17.241885l-19.476944-98.023307a63.858832 63.858832 0 0 0-63.858832-51.40636h-85.890129a63.858832 63.858832 0 0 0-63.858832 51.40636l-19.476944 98.023307a386.02664 386.02664 0 0 0-41.188947 17.241885L261.195169 111.126182a63.858832 63.858832 0 0 0-80.462128 7.982354L119.109268 180.732309a63.858832 63.858832 0 0 0-7.982354 80.462128l55.557184 83.016482a383.152992 383.152992 0 0 0-17.241885 41.188946l-98.023307 19.476944a63.858832 63.858832 0 0 0-51.40636 63.858832v86.848012a63.858832 63.858832 0 0 0 51.40636 63.858832l98.023307 19.476944a385.707346 385.707346 0 0 0 17.241885 41.188946l-55.557184 83.016482a63.858832 63.858832 0 0 0 7.982354 80.462129l61.304478 61.304478a63.858832 63.858832 0 0 0 80.462129 7.982354l83.016482-55.557184a379.321462 379.321462 0 0 0 41.188946 17.241885l19.476944 98.023307a63.858832 63.858832 0 0 0 63.858832 51.40636h86.848012a63.858832 63.858832 0 0 0 63.858832-51.40636l19.796238-98.023307a385.707346 385.707346 0 0 0 41.188946-17.241885l83.016482 55.557184a63.858832 63.858832 0 0 0 80.462128-7.982354l61.304479-61.304478a63.858832 63.858832 0 0 0 7.982354-80.462129l-55.557184-83.016482a375.809227 375.809227 0 0 0 17.241885-41.188946l97.704013-19.476944a63.858832 63.858832 0 0 0 51.40636-63.858832v-87.805894a63.858832 63.858832 0 0 0-53.002831-62.581656zM860.191014 575.060597a63.858832 63.858832 0 0 0-47.894124 41.508241 325.360749 325.360749 0 0 1-13.729649 33.525886 63.858832 63.858832 0 0 0 4.150824 63.858832l55.557184 83.016482-61.304479 61.304479-83.016482-55.557184a63.858832 63.858832 0 0 0-63.858832-4.470118 321.848514 321.848514 0 0 1-34.483769 14.368237 63.858832 63.858832 0 0 0-41.508241 47.894124L554.626502 958.213589h-86.848011L447.343664 860.190282a63.858832 63.858832 0 0 0-41.50824-47.894124 319.29416 319.29416 0 0 1-33.525887-13.729649 63.858832 63.858832 0 0 0-63.858832 4.470118l-83.016482 55.557184-60.985185-61.943067 55.557184-83.016482a63.858832 63.858832 0 0 0 4.470119-63.858832 321.209925 321.209925 0 0 1-14.368238-34.483769A63.858832 63.858832 0 0 0 162.213979 575.060597L64.190672 554.62577v-86.848011L162.213979 447.342933a63.858832 63.858832 0 0 0 47.894124-41.508241A319.29416 319.29416 0 0 1 223.837752 372.308805a63.858832 63.858832 0 0 0-4.470118-63.858832L164.449038 225.752785l61.304479-61.304479 83.016482 55.557184a63.858832 63.858832 0 0 0 63.858832 4.470119 319.29416 319.29416 0 0 1 34.164475-14.368238A63.858832 63.858832 0 0 0 447.343664 162.213247L467.778491 64.18994h86.848011L575.061329 162.213247a63.858832 63.858832 0 0 0 41.50824 47.894124 319.29416 319.29416 0 0 1 33.525887 13.729649 63.858832 63.858832 0 0 0 63.858832-4.470118l83.016482-55.557184 61.304479 61.304479-55.557184 83.016482a63.858832 63.858832 0 0 0-4.470118 63.858832 321.209925 321.209925 0 0 1 14.368237 34.483769 63.858832 63.858832 0 0 0 47.894124 41.508241l97.704013 19.476944v86.848011z"
                          fill="#ffffff" p-id="1916"></path>
                    <path d="M511.202497 287.695852a223.505912 223.505912 0 1 0 223.505912 223.505913 223.505912 223.505912 0 0 0-223.505912-223.505913z m0 419.233233A195.72732 195.72732 0 1 1 706.929817 511.201765 195.72732 195.72732 0 0 1 511.202497 706.929085z"
                          fill="#ffffff" p-id="1917"></path>
                    <path d="M511.202497 383.4841a127.717664 127.717664 0 1 0 127.717664 127.717665 127.717664 127.717664 0 0 0-127.717664-127.717665z m0 223.505913a95.788248 95.788248 0 1 1 95.788248-95.788248 95.788248 95.788248 0 0 1-95.788248 95.788248z"
                          fill="#ffffff" p-id="1918"></path>
                </svg>
                <svg t="1555469437620" class="img" id="small-wheel" style="" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="2112" xmlns:xlink="http://www.w3.org/1999/xlink"
                     width="25" height="25">
                    <defs>
                        <style type="text/css"></style>
                    </defs>
                    <path d="M512 315.9c-108.3 0-196.1 87.8-196.1 196.1S403.7 708.1 512 708.1 708.1 620.3 708.1 512 620.3 315.9 512 315.9z m93.41 289.51A132.11 132.11 0 1 1 644.1 512a131.25 131.25 0 0 1-38.69 93.41z"
                          p-id="2113" fill="#ffffff"></path>
                    <path d="M851.37 512c0-7.13-0.23-14.33-0.69-21.54l68.83-62.15-4.73-18.69a413.07 413.07 0 0 0-45.62-110L859.29 283l-92.59 4.73a340.49 340.49 0 0 0-30.45-30.45l4.75-92.57-16.56-9.87a413.07 413.07 0 0 0-110-45.62l-18.69-4.74-62.15 68.84a336.56 336.56 0 0 0-43.08 0l-62.15-68.84-18.69 4.74a413.07 413.07 0 0 0-110 45.62L283 164.71l4.73 92.59a341.81 341.81 0 0 0-30.45 30.45L164.71 283l-9.87 16.56a413.07 413.07 0 0 0-45.62 110l-4.74 18.69 68.84 62.15c-0.46 7.21-0.69 14.41-0.69 21.54s0.23 14.33 0.69 21.54l-68.84 62.15 4.74 18.69a413.07 413.07 0 0 0 45.62 110l9.87 16.68 92.59-4.73a340.49 340.49 0 0 0 30.45 30.45L283 859.29l16.56 9.87a413.07 413.07 0 0 0 110 45.62l18.69 4.73 62.15-68.83a336.56 336.56 0 0 0 43.08 0l62.15 68.83 18.69-4.73a413.07 413.07 0 0 0 110-45.62l16.68-9.87-4.73-92.59a341.81 341.81 0 0 0 30.45-30.45l92.57 4.75 9.87-16.56a413.07 413.07 0 0 0 45.62-110l4.73-18.69-68.83-62.15c0.46-7.27 0.69-14.47 0.69-21.6z m-65.75-31.06a276.51 276.51 0 0 1 0 62.12l-1.83 16.32L847.57 617a349.88 349.88 0 0 1-24.1 58.1l-85.81-4.38-10.25 12.85a277.6 277.6 0 0 1-43.88 43.88l-12.85 10.25 4.38 85.81a349.88 349.88 0 0 1-58.1 24.1l-57.58-63.78-16.32 1.83a276.51 276.51 0 0 1-62.12 0l-16.32-1.83L407 847.57a349.88 349.88 0 0 1-58.1-24.1l4.38-85.81-12.85-10.25a277.66 277.66 0 0 1-43.89-43.88l-10.24-12.85-85.81 4.38A349 349 0 0 1 176.44 617l63.77-57.58-1.83-16.32a276.51 276.51 0 0 1 0-62.12l1.83-16.32L176.43 407a349.88 349.88 0 0 1 24.1-58.1l85.81 4.38 10.24-12.85a278.57 278.57 0 0 1 43.89-43.89l12.85-10.24-4.38-85.81a349.88 349.88 0 0 1 58.1-24.1l57.58 63.78 16.32-1.83a276.51 276.51 0 0 1 62.12 0l16.32 1.83L617 176.44a349 349 0 0 1 58.1 24.09l-4.38 85.81 12.85 10.24a277.66 277.66 0 0 1 43.88 43.89l10.25 12.85 85.81-4.38a349.88 349.88 0 0 1 24.1 58.1l-63.78 57.58z"
                          p-id="2114" fill="#ffffff"></path>
                </svg>
            </div>

        </div>

        <div id="msg-div">
            工具运行出错
        </div>

    </div>

    <div id="reload-div">
        <button onclick="javascript:window.location.reload()">刷&nbsp;新</button>
    </div>

    <p id="error-msg">
        错误信息 : ${ msg }
    </p>
</div>

</body>
</html>

