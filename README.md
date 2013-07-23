## Overview
This hack was inspired by the [RGB visualization](http://io9.com/5985749/the-worlds-largest-known-prime-number-in-rgb)
of the [largest known prime number](http://www.isthe.com/chongo/tech/math/digit/m57885161/prime-c.html).

It essentially constructs a 1000x1000 gray-scale image by setting each individual (x,y) pixel to a color that trends
from black to white depending on how frequently the (x,y) pair appears adjacent to each other in the prime number.

For example, given this partial sequence of the most significant digits in the prime:
```
581,887,266,232,246,442,175,100,212,113,232,368,636,370,...
```

The algorithm records the number of times (x=581,y=887), (x=266,y=232), and so on, occurs in the sequence. Note the
distribution of pairs below.

## Distribution of pairs
|Frequency of pairs|Unique pairs|
|0|54934|
|1|159358|
|2|231035|
|3|223434|
|4|161851|
|5|94599|
|6|45803|
|7|18958|
|8|6816|
|9|2376|
|10|620|
|11|171|
|12|38|
|13|5|
|14|2|
