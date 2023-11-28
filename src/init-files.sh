#!/bin/bash
seq -f %02g 1 25 | awk -v q='"' '{d2=$0; d=1*$0;
    # clone kt files: Day0x.kt
    printf "cp template.kt Day%s.kt\n", d2

    # clone input files: inputs/Day0x.txt and inputs/Day0x-test.txt
    printf "touch inputs/Day%s.txt; touch inputs/Day%s-test.txt\n", d2, d2

    #fix the day numbers in kt files. template has '1x'
    printf "sed -i %s1 s#day/1x#day/%s#%s Day%s.kt\n", q, d, q, d2
    printf "sed -i %ss#Day1x#Day%s#%s Day%s.kt\n", q, d2, q, d2
}'

# pipe to "sh" if output looks ok
