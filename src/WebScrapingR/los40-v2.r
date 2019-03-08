library(rvest)

countries <- c("España", "Colombia", "Mexico", "Chile", "Argentina", "Costa Rica", "Panamá")
ccode <- c("es", "co", "mx", "ch", "ar", "cr", "pa")
urls <- c("los40.com", "los40.com.co", "los40.com.mx", "los40.cl", "los40.com.ar", "los40.co.cr", "los40.com.pa")
inityear <- 2010
endyear <- 2019
lastweek <- 53  # 2010:52; 2011:53; 2012:52; 2013:52; 2014:52; 2015:52; 2016:53; 2017:31

for (country in 1:length(countries)) {
  for (year in inityear:endyear) {
    pos <- NULL
    artist <- NULL
    graph <- data.frame(s=character(), t=character(), ty=character(), w=double())
    lst <- data.frame(p=integer(), n=integer(), a=character())
    
    for (week in 1:lastweek) {
      print(paste("Processing year", year, "and week", week, "for", countries[country]))
      url <- paste("http://", urls[country], "/lista40/", year, "/", week, sep="")
      html <- read_html(url)
      articles <- html %>% html_nodes("div.article")
      pos <- articles %>% html_nodes("span.posicion") %>% html_text()
      artists <- articles %>% html_nodes("h4 a") %>% html_text()
      if (length(artists)==0) { break }
      
      for (a in 1:40) {
        artist <- strsplit(artists[a], ";")[[1]]
        p <- pos[a]
        if (is.na(p)) { break }
        lst <- rbind(lst, data.frame(as.numeric(p), length(artist), gsub(" »", "", artists[a])))
        
        if (length(artist)>1) {
          for (i in 1:length(artist)) {
            if (length(artist)>=(i+1)) {
              for (j in (i+1):length(artist)) {
                s <- gsub(" »", "", artist[i])
                t <- gsub(" »", "", artist[j])
                w <- 40 / as.numeric(p)
                graph <- rbind(graph, data.frame(s, t, "undirected", w))
              }
            }
          }
        }
      }
      
      Sys.sleep(0.5)
    }
    print("Writing graph")
    colnames(graph) <- c("source", "target", "type", "weight")
    write.csv(graph, paste("los40-", ccode[country], "-", year, "-graph.csv", sep=""), row.names=FALSE)
    
    colnames(lst) <- c("position", "num_authors", "authors")
    write.csv(lst, paste("los40-", ccode[country], "-", year, "-list.csv", sep=""), row.names=FALSE)
  }
}

