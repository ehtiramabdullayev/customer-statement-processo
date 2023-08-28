::: {#header}
# Rabobank Customer Statement Processor
:::

::: {#content}
::: {#preamble}
::: sectionbody
::: paragraph
Rabobank receives monthly deliveries of customer statement records. This
information is delivered in two formats, CSV and XML. These records need
to be validated.
:::
:::
:::

::: sect1
## Input {#_input}

::: sectionbody
::: paragraph
The format of the file is a simplified version of the MT940 format. The
format is as follows:
:::

+-----------------------------------+-----------------------------------+
| Field                             | Description                       |
+===================================+===================================+
| Transaction reference             | A numeric value                   |
+-----------------------------------+-----------------------------------+
| Account number                    | An IBAN                           |
+-----------------------------------+-----------------------------------+
| Start Balance                     | The starting balance in Euros     |
+-----------------------------------+-----------------------------------+
| Mutation                          | Either an addition (+) or a       |
|                                   | deduction (-)                     |
+-----------------------------------+-----------------------------------+
| Description                       | Free text                         |
+-----------------------------------+-----------------------------------+
| End Balance                       | The end balance in Euros          |
+-----------------------------------+-----------------------------------+

: Table 1. Record description
:::
:::

::: sect1
## Output {#_output}

::: sectionbody
::: paragraph
There are two validations:
:::

::: ulist
-   all transaction references should be unique

-   the end balance needs to be validated
    :::

::: paragraph
At the end of the processing, a report needs to be created which will
display both the transaction reference and description of each of the
failed records.
:::
:::
:::
:::

::: {#footer}
::: {#footer-text}
Last updated 2021-02-25 17:04:53 +0100
:::
:::
