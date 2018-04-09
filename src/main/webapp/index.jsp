<html>
<body>
<h2>Hello World!</h2>

springmvc文件上传
<form action="/manage/movie/ upload.do" name="form1" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件">
</form>
富文本文件上传
<form action="/manage/movie/richtextImgUpload.do" name="form1" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件"/>
</form>
</body>
</html>
