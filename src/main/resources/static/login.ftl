<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><#if title?has_content>${title}</#if></title>
    <link rel="stylesheet" type="text/css" href="/assets/css/fonts.css" />
</head>

<body>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="border-style: none; margin: 0;">
    <tr bgcolor="black">
        <td height="50" nowrap="nowrap" width="${menuLeftSize}" style="padding: 0;">
            <#if caption?? && caption?has_content>
                <a href="/" style="margin-left: 20px; font-family: RobotoRegular; text-decoration: none; color: white; font-size: 25px;">${caption}</a>
            </#if>
        </td>
    </tr>
    <tr>
        <td style="padding-left: 20px; padding-right: 20px;" valign="top" width="100%" height="100%">
            LOGIN
        </td>
    </tr>
    <#if footer?? && footer?has_content>
        <tr>
            <td align="center" style="font-family: RobotoRegular;">${footer}</td>
        </tr>
    </#if>
</table>

</body>
</html>