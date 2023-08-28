``html
<html lang="en">
<body class="article">
<div id="header">
    <h1>Rabobank Customer Statement Processor</h1>
</div>
<div id="content">
    <div id="preamble">
        <div class="sectionbody">
            <div class="paragraph">
                <p>Rabobank receives monthly deliveries of customer statement records. This information is delivered in two formats, CSV and XML. These records need to be validated.</p>
            </div>
        </div>
    </div>
    <div class="sect1">
        <h2 id="_input">Input</h2>
        <div class="sectionbody">
            <div class="paragraph">
                <p>The format of the file is a simplified version of the MT940 format. The format is as follows:</p>
            </div>
            <table class="tableblock frame-all grid-all stretch">
                <caption class="title">Table 1. Record description</caption>
                <colgroup>
                    <col style="width: 50%;">
                    <col style="width: 50%;">
                </colgroup>
                <thead>
                <tr>
                    <th class="tableblock halign-left valign-top">Field</th>
                    <th class="tableblock halign-left valign-top">Description</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Transaction reference</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">A numeric value</p></td>
                </tr>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Account number</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">An IBAN</p></td>
                </tr>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Start Balance</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">The starting balance in Euros</p></td>
                </tr>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Mutation</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Either an addition (+) or a deduction (-)</p></td>
                </tr>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Description</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">Free text</p></td>
                </tr>
                <tr>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">End Balance</p></td>
                    <td class="tableblock halign-left valign-top"><p class="tableblock">The end balance in Euros</p></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="sect1">
        <h2 id="_output">Output</h2>
        <div class="sectionbody">
            <div class="paragraph">
                <p>There are two validations:</p>
            </div>
            <div class="ulist">
                <ul>
                    <li>
                        <p>all transaction references should be unique</p>
                    </li>
                    <li>
                        <p>the end balance needs to be validated</p>
                    </li>
                </ul>
            </div>
            <div class="paragraph">
                <p>At the end of the processing, a report needs to be created which will display both the transaction reference and description of each of the failed records.</p>
            </div>
        </div>
    </div>
</div>
<div id="footer">
    <div id="footer-text">
        Last updated 2021-02-25 17:04:53 +0100
    </div>
</div>
</body>
</html>
``