# TextGridHttpServerPlugin
This a a plugin for the [TextGrid Lab](https://textgrid.de/) software which serves the personal repository locally via HTTP. This is useful if the edition project uses previews (e.g. via XSLT or HTML files) which make use of linked javascript and images, which are also stored inside the TextGrid repository.

# Installation
This plugin is available via the TextGrid Marketplace. Please start TextGrid Lab, click on Marketplace, search for TextGridHttpServerPlugin and click on install.

## Manual install
Please go to the release tab on Github, and then download the latest Zip file. Start TextGrid Lab, go to the menu Help -> Install new software, click on the button Add..., click on the button archive and select the downloaded Zip file. Select the TextGridHttpServerPlugin feature, confirm the installation and restart TextGrid Lab.

After restart of TextGrid Lab installation, please start a web browser and open the following URL: http://localhost:9090

If the installation has been successful, you will see this welcome page:

![welcome page](https://raw.githubusercontent.com/Hannah-Arendt-Project/TextGridHttpServerPlugin/master/gh-imgs/indexpage.png)

If Windows asks you you want to allow the network access, please confirm. This HTTP server can only be reached locally and not via any network connection.

# Usage
* Expand your TextGrid project with the Navigator and select your object you want to access via a HTML or XSLT. Right click on it and select copy URI.
* Open your HTML or XSLT document and edit the corresponding src/url tags. For example change `<script type="text/javascript" src="myscript.js">` to `<script type="text/javascript" src="http://localhost:9090/textgrid:345c.0">`. The textgrid:345c.0 path is the URI which you have copied into the clipboard before. This can be done with any tags which have a src or href attribute (e.g. `<img src="mybackground.jpg"/>` needs to be edited to `<img src=""http://localhost:9090/textgrid:367a.0"/>`)
* Now perform your XSLT or open the HTML document inside Textgrid. The linked images/scripts etc. which are stored in the TextGrid repository are now available for this document.

# License
Apache 2.0.

# Credits
This plugin makes use of the [NannoHTTPD Java Web server](https://github.com/NanoHttpd/nanohttpd). 

<a href="http://www.sub.uni-goettingen.de"><img src="https://raw.githubusercontent.com/Hannah-Arendt-Project/TextGridHttpServerPlugin/master/gh-imgs/sub-logo.jpg" width="300"/></a>
