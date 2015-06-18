##########################################################################################################################
# This code has been developed by
#   ANDRES RAMOS PEREZ, AUTORITAS CONSULTING, SPAIN
#   andres.ramos@autoritas.es
#   ID: arp
#
#Description of main functions:
#   Function            | Submission | MAE   | Execution             | Description
#   ----------------------------------------------------------------------------------------------------------------  
#   MyPrediction        | 2          | 2.556 | MyPrediction()        | It doesn't consider the holidays.
#   MyPredictionHoliday | 3          | 2.558 | MyPredictionHoliday() | It considers the holidays and sundays.
##########################################################################################################################

library(reshape2)
library(pmml)
library(XML)
library(data.table)



dirpaste = function(...) paste(...,sep="/")
str2vec = function(s,split=",") unlist(strsplit(s,split=split))

D_DIR<-'./deployment_data'



#######################################################
load_from_pmml = function(filename) {
  res = xmlRoot(xmlTreeParse(readLines(filename,warn=FALSE)))
  rt = res[[3]][[3]]
  rt = res[["RegressionModel"]][["RegressionTable"]]
  intercept = as.numeric(xmlAttrs(rt))
  coeff = NULL
  nam = NULL
  for (i in seq_along(rt)) {
    coeff[i] = as.numeric(xmlAttrs(rt[[i]])["coefficient"])
    nam[i] = xmlAttrs(rt[[i]])["name"]
  }
  names(coeff) = nam
  n_coeff = length(coeff)
  mat = matrix(runif(n_coeff^2+n_coeff),ncol=n_coeff)
  df = data.frame(mat)
  colnames(df) = nam
  df$med_available = apply(mat,1,sum)
  mod = lm(med_available~.,df)
  mod$coefficients[1] = intercept
  mod$coefficients[2:length(mod$coefficients)] = coeff
  return(mod)
}



#######################################################
### Works out MAE between two vectors ###
calc_mae = function(x,y) { mean(abs(x-y)) }



#######################################################
### Main function of submission 2
#MyPrediction
MyPrediction <- function() {
    tf = read.csv("test_data_for_leaderboard.csv")
  tf$bikes <- 0
  ncols_tf = ncol(tf)
  
  #Reading the distance among stations
  sdf = read.csv("Stations_Distance_Table.csv")

  #Add a column at first position with the id of station (1,..., 275)
  id_station<-names(sdf)
  sdf = cbind(id_station, sdf)
  
  nrows_tf = nrow(tf)
  
  #For each row of file test
  for (i in 1:nrows_tf) {
    cat('\n########### Row:',i,' ###########\n')
    
    bm_tf = data.frame()
    
    test_station = tf[i,]$station
    
    #test_stationX is the read station from test file. It is prefixed with the letter x
    test_stationX = paste("X",test_station, sep = "")
    
    #Distance of the selected rental station of the test file with respect to the others
    sdfSel = sdf[,c("id_station", test_stationX)]
    
    #Stations with id less than 201
    sdfSel <- sdfSel[1:200,]
    
    
    #Renaming the second column to order. Deleting the distance to the main station
    colnames(sdfSel)[2] <- "distance"
    sdfSel <- sdfSel[order(sdfSel$distance),] 
    sdfSel <- sdfSel[2:nrow(sdfSel),]
    
    #Selecting stations whose distance is less than 0.7 kilometers.
    sdfSel07 <- sdfSel[(which(sdfSel$distance < 0.7)),]
    nrows_sdfSel07 = nrow(sdfSel07)
    
    #Selecting stations whose distance is less than 1 kilometer.
    if (nrows_sdfSel07==0) {
      sdfSel07 <- sdfSel[(which(sdfSel$distance < 1.0)),]
      nrows_sdfSel07 = nrow(sdfSel07)
    } 
    
    for (j in 1:nrows_sdfSel07) {
      sdfSel07_eval <- as.character(sdfSel07[j,1])
      sdfSel07_eval = unlist(strsplit(sdfSel07_eval, split='X', fixed=TRUE))[2]
      sdfSel07_eval = as.integer(sdfSel07_eval)
      
      #Constructing the subset of data
      dt = as.data.table(read.csv(sprintf("deployment_data/station_%d_deploy_full.csv",sdfSel07_eval)))
      weekday_sel = as.character(tf[i,]$weekday)
      hour_sel = as.character(tf[i,]$hour)
      dt <- dt[(which(dt$weekday == weekday_sel))]
      dt <- dt[(which(dt$hour == hour_sel))]
      
      #Selecting the best model for the station
      bm = BestStationModel(sdfSel07_eval, dt)
      
      #cat('\nThe best model for the station is ',sdfSel07_eval,' es ',bm)
      
      bm_tf[j,1] = bm
    }
    
    #Best models of the nearest stations
    bm_tf = unique(bm_tf)
    
    nrows_bm_tf = nrow(bm_tf)
    
    #Calculating the prediction with the best models
    for (k in 1:nrows_bm_tf) {
      #cat("Modelo a predecir...",bm_tf[k,1],"\n")
      
      #Selecting the model
      mod = load_from_pmml(sprintf("linear_models/%s", bm_tf[k,1]))
      
      #Applying the model
      pred = predict(mod,tf[i,])      
      
      pred[is.na(pred)] = 0   
      
      bm_tf[k,2] = pred
    }
    
    tf[i,ncols_tf] = mean(bm_tf[,2])
    
  }

  write.csv(tf,file="DS_arp_sub2.csv",row.names=FALSE) 
  
  salida = read.csv("DS_arp_sub2.csv")
  salida = salida[, c("station", "timestamp", "bikes")]
  write.csv(salida,file="DS_arp_submission2.csv",row.names=FALSE)
  
}



#######################################################
### Main function of submission 3
#MyPredictionHoliday
MyPredictionHoliday <- function() {
  tf = read.csv("test_data_for_leaderboard.csv")
  tf$bikes <- 0
  ncols_tf = ncol(tf)
  
  #Reading the distance among stations
  sdf = read.csv("Stations_Distance_Table.csv")
  
  #Add a column at first position with the id of station (1,..., 275)
  id_station<-names(sdf)
  sdf = cbind(id_station, sdf)
  
  nrows_tf = nrow(tf)
  
  #For each row of file test
  for (i in 1:nrows_tf) {
    cat('\n########### Row:',i,' ###########\n')
    
    bm_tf = data.frame()
    
    test_station = tf[i,]$station
    
    #test_stationX is the read station from test file. It is prefixed with the letter x
    test_stationX = paste("X",test_station, sep = "")
    
    #Distance of the selected rental station of the test file with respect to the others
    sdfSel = sdf[,c("id_station", test_stationX)]
    
    #Stations with id less than 201
    sdfSel <- sdfSel[1:200,]
    
    
    #Renaming the second column to order. Deleting the distance to the main station
    colnames(sdfSel)[2] <- "distance"
    sdfSel <- sdfSel[order(sdfSel$distance),] 
    sdfSel <- sdfSel[2:nrow(sdfSel),]
    
    #Selecting stations whose distance is less than 0.7 kilometers.
    sdfSel07 <- sdfSel[(which(sdfSel$distance < 0.7)),]
    nrows_sdfSel07 = nrow(sdfSel07)
    
    #Selecting stations whose distance is less than 1 kilometer.
    if (nrows_sdfSel07==0) {
      sdfSel07 <- sdfSel[(which(sdfSel$distance < 1.0)),]
      nrows_sdfSel07 = nrow(sdfSel07)
    } 
    
    for (j in 1:nrows_sdfSel07) {
      sdfSel07_eval <- as.character(sdfSel07[j,1])
      sdfSel07_eval = unlist(strsplit(sdfSel07_eval, split='X', fixed=TRUE))[2]
      sdfSel07_eval = as.integer(sdfSel07_eval)
      
      #Constructing the subset of data
      dt = as.data.table(read.csv(sprintf("deployment_data/station_%d_deploy_full.csv",sdfSel07_eval)))
      
      isHoliday_sel = as.character(tf[i,]$isHoliday)
      hour_sel = as.character(tf[i,]$hour)
      
      #Selecting all the holidays and sundays
      if (isHoliday_sel==1) {
        dt <- dt[(which(dt$weekday == "Sunday" | dt$isHoliday == isHoliday_sel))]
      } else {
        weekday_sel = as.character(tf[i,]$weekday)
        dt <- dt[(which(dt$weekday == weekday_sel))]
      }
      
      dt <- dt[(which(dt$hour == hour_sel))]
      
      
      #Selecting the best model for the station
      bm = BestStationModel(sdfSel07_eval, dt)
      
      #cat('\nThe best model for the station is ',sdfSel07_eval,' es ',bm)
      
      bm_tf[j,1] = bm
    }
    
    #Best models of the nearest stations
    bm_tf = unique(bm_tf)
    
    nrows_bm_tf = nrow(bm_tf)
    
    #Calculating the prediction with the best models
    for (k in 1:nrows_bm_tf) {
      #cat("Modelo a predecir...",bm_tf[k,1],"\n")
      
      #Selecting the model
      mod = load_from_pmml(sprintf("linear_models/%s", bm_tf[k,1]))
      
      #Applying the model
      pred = predict(mod,tf[i,])      
      
      pred[is.na(pred)] = 0   
      
      bm_tf[k,2] = pred
    }
    
    tf[i,ncols_tf] = mean(bm_tf[,2])
    
  }
  
  write.csv(tf,file="DS_arp_sub3.csv",row.names=FALSE) 
  
  salida = read.csv("DS_arp_sub3.csv")
  salida = salida[, c("station", "timestamp", "bikes")]
  write.csv(salida,file="DS_arp_submission3.csv",row.names=FALSE)
  
}



###############################################################
### MyEvaluateStation
MyEvaluateStation <- function(num_station, dt) {
  
  truth = dt$bikes
  
  tt = !is.na(truth)
  
  base_files = sort( grep("xml", (list.files(path="./linear_models", pattern="model_base_*")), value=TRUE) )
  stat_files = grep("xml", (list.files(path="./linear_models", pattern="model_station_*")), value=TRUE)
  #Select only those models from the same station
  stat_files = sort( grep(sprintf("_%d_",num_station), stat_files, value=TRUE) )
  
  #Selecting the general pursposing and specific models for the rental station
  mod_files  = c(base_files,stat_files)
  
  res = data.frame()
  
  for (m in seq_along(mod_files)) {
    mod = load_from_pmml(sprintf("linear_models/%s", mod_files[m]))
  
    pred = predict(mod,dt)
    
    pred[is.na(pred)] = 0
    
    mae = calc_mae(pred[tt],truth[tt])
    
    res = rbind( res, data.frame(station=num_station, model=strsplit(mod_files[m],"\\.")[[1]][1], mae=mae) )
  }
  res
}



###############################################################
### BestStationModel
#It evaluates all the models of a station and returns the model with less MAE
BestStationModel <- function(station, dt) {
  
  es_res = MyEvaluateStation(station, dt)
  es_res_string = as.character(es_res[,2])
  best_model_id = which.min(es_res[,3])
  best_model_name = es_res_string[best_model_id]
  
  res = paste(best_model_name,'.xml',sep = "")
  res
}



###############################################################
### earth.dist
### Calculate distance in kilometers between two points
earth.dist <- function (long1, lat1, long2, lat2)
{
  radians <- pi/180
  
  lat1_rad <- lat1 * radians
  long1_rad <- long1 * radians
  lat2_rad <- lat2 * radians
  long2_rad <- long2 * radians
  
  long_dist <- long2_rad - long1_rad
  lat_dist <- lat2_rad - lat1_rad
  
  val_a <- (sin(lat_dist/2))^2 + cos(lat1_rad) * cos(lat2_rad) * (sin(long_dist/2))^2
  val_c <- 2 * atan2(sqrt(val_a), sqrt(1 - val_a))
  res <- 6378.145
  res_end <- res * val_c
  
  return(res_end)
}


#######################################################
StationDistanceTable <- function() {
  #Ex: StationDistanceTable()
  
  dir_files = dir(path=D_DIR, pattern="*.csv")
  res = data.frame()
  sta = data.frame()
  
  for ( m in 1:length(dir_files) ) {
    num_station1<-strsplit(dir_files[m],"_")[[1]][2]
    f1 = read.csv(dirpaste(D_DIR,dir_files[m])) #na.strings='NA')
    lat1<-f1[1,2]
    long1<-f1[1,3]
    sta = rbind( sta, data.frame(station=num_station1, LATI=lat1, LONG=long1) )
  }
  
  sta[,1]<-as.integer(sta[,1])
  sta[,2]<-as.double(sta[,2])
  sta[,3]<-as.double(sta[,3])
  
  sta <- sta[ order(sta[,1]), ]
  
  mm <- matrix(0, nrow(sta), nrow(sta))
  
  for ( m in 1:(nrow(sta)-1) ) {
    num_station1<-sta[m,1]
    lat1<-sta[m,2]
    lon1<-sta[m,3]
    
    for ( n in (m+1):nrow(sta) ) {
      num_station2<-sta[n,1]
      lat2<-sta[n,2]
      long2<-sta[n,3]
      
      mm[num_station1,num_station2] = earth.dist(long1, lat1, long2, lat2)
      mm[num_station2,num_station1] = mm[num_station1,num_station2]
    }
  }
  DF<-as.data.frame(mm)
  colnames(DF)<-c(1:(ncol(DF)))
  write.csv(DF,file="Stations_Distance_Table.csv",row.names=FALSE)
}