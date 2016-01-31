# precis_for_gist
precis_for_gist is rewriting precis for
1. Being fully in memory
2. Efficiency using
  i.   Multi Threading
  ii.  Finding only Frequent Item Sets without getting actual numbers during the multi stage phase
  iii. Using the trick of Apriori+
3. Configurability with user input. When the user submits a file or directory containing files in GIST for analysis,
  i.   We run an initial column level group by on each column
  ii.  Find columns with same values to omit
  iii. Find columns with a distinct value for each row so as to omit.
  iv.  Find columns where one value is very dominant and is more than 50%. Offer to run PRECIS for rows with that values and rows without.
  v.   List all distinct values and counts for each dimension and allow the user to block the tracking of this value. This is really useful for removing certain domninant column values that are futile like null, n/a, empty string etc. This makes GIST reveal only useful frequent itemsets. It also speeds up the total processing of GIST.
  vi.  For columns with low count of distint values(cardinality) all of which are fairly well represented, like Gender (which has 2 distinct values male & female), offer to run PRECIS for each separate sets of data for each value of the column. This would only apply to columns from 2 to 4 distinct values.
  vii. If more than 25 columns in the table, pick only top 25 columns of interest and start a run of PRECIS.
  
As for benchmarks, we will have to compare this with results from R's libraries. We will also have a minimum of 2% of total metric value of PRECIS as the benchmark. Anything below 2% leads to unnavigable bloat.
