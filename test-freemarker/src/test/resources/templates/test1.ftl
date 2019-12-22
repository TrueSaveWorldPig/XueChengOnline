<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title></head>
<body>
Hello ${name}!
<hr/>
list指令
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
        <td>出生日期</td>
    </tr>
    <#list stus as stu>
        <tr>
            <td>${stu_index}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.money}</td>
            <td>${stu.birthday?string('yyyy年MM月dd日')}</td>
        </tr>
    </#list>
</table>
<hr/>
遍历map集合
方式1
<br>
<span>${stuMap['stu1'].name}</span>
<br>
<span>${stuMap['stu2'].name}</span>
方式2
<br>
<span>${stuMap.stu1.name}</span>
<br>
<span>${stuMap.stu2.name}</span>
方式3,依赖list指令
<br>
<#list stuMap?keys as k>
    <span <#if stuMap[k].name=='小明'>style="background-color: red" </#if>>${stuMap[k].name}</span>
    <span>${stuMap[k].age}</span>
</#list>

<#if stuMap??><h1>你哈</h1></#if>
<#if stuMap??><h1>${stuMap?size}</h1></#if>
</body>
</html>