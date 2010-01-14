
rm(list=ls())

path<-"~/dev/hsm/PhenoPermutation/src/test/resources/"

testlogfile<-paste(path,"Testwerte.txt",sep="")
basiclogfile<-paste(path,"Basiswerte.txt",sep="")
file.create(testlogfile)
file.create(basiclogfile)
hap.file<-paste(path,"hsm.hap",sep="")
pheno.file<-paste(path,"hsm.pheno",sep="")

haps<-readLines(hap.file)
ph<-read.table(pheno.file,header=F)


N <- 3

haps.vec<-rep(NA,6)

haps.vec[1]<-haps[3]
haps.vec[2]<-haps[4]
haps.vec[3]<-haps[6]
haps.vec[4]<-haps[7]
haps.vec[5]<-haps[9]
haps.vec[6]<-haps[10]

print(haps.vec)
# Phaenotypische Aehnlichkeit

pp<-rep(NA,3)

pp[1] <- ph[1,2]
pp[2] <- ph[2,2]
pp[3] <- ph[3,2]

mu<-sum(pp)/length(pp)

print(paste("mu == ", mu, sep=""))

# 1. Person mit 2. Person

Y <- matrix(data=NA,ncol=6,nrow=6)
diag(Y)<-0

X1 <- matrix(data=NA,ncol=6,nrow=6)
diag(X1)<-0
X2 <- matrix(data=NA,ncol=6,nrow=6)
diag(X2)<-0
X3 <- matrix(data=NA,ncol=6,nrow=6)
diag(X3)<-0
X4 <- matrix(data=NA,ncol=6,nrow=6)
diag(X4)<-0
X5 <- matrix(data=NA,ncol=6,nrow=6)
diag(X5)<-0


# 1. haplotey w/ other haplotypes
X1[1,2]<-X1[2,1]<-5
X2[1,2]<-X2[2,1]<-5
X3[1,2]<-X3[2,1]<-5
X4[1,2]<-X4[2,1]<-5
X5[1,2]<-X5[2,1]<-5

X1[1,3]<-X1[3,1]<-0
X2[1,3]<-X2[3,1]<-1
X3[1,3]<-X3[3,1]<-0
X4[1,3]<-X4[3,1]<-2
X5[1,3]<-X5[3,1]<-2

X1[1,4]<-X1[4,1]<-0
X2[1,4]<-X2[4,1]<-0
X3[1,4]<-X3[4,1]<-1
X4[1,4]<-X4[4,1]<-0
X5[1,4]<-X5[4,1]<-1

X1[1,5]<-X1[5,1]<-4
X2[1,5]<-X2[5,1]<-4
X3[1,5]<-X3[5,1]<-4
X4[1,5]<-X4[5,1]<-4
X5[1,5]<-X5[5,1]<-0

X1[1,6]<-X1[6,1]<-1
X2[1,6]<-X2[6,1]<-0
X3[1,6]<-X3[6,1]<-0
X4[1,6]<-X4[6,1]<-0
X5[1,6]<-X5[6,1]<-1

# 2. haplotey w/ other haplotypes
X1[2,3]<-X1[3,2]<-0
X2[2,3]<-X2[3,2]<-1
X3[2,3]<-X3[3,2]<-0
X4[2,3]<-X4[3,2]<-2
X5[2,3]<-X5[3,2]<-2

X1[2,4]<-X1[4,2]<-0
X2[2,4]<-X2[4,2]<-0
X3[2,4]<-X3[4,2]<-1
X4[2,4]<-X4[4,2]<-0
X5[2,4]<-X5[4,2]<-1

X1[2,5]<-X1[5,2]<-4
X2[2,5]<-X2[5,2]<-4
X3[2,5]<-X3[5,2]<-4
X4[2,5]<-X4[5,2]<-4
X5[2,5]<-X5[5,2]<-0

X1[2,6]<-X1[6,2]<-1
X2[2,6]<-X2[6,2]<-0
X3[2,6]<-X3[6,2]<-0
X4[2,6]<-X4[6,2]<-0
X5[2,6]<-X5[6,2]<-1


# 3. haplotey w/ other haplotypes
X1[3,4]<-X1[4,3]<-1
X2[3,4]<-X2[4,3]<-0
X3[3,4]<-X3[4,3]<-0
X4[3,4]<-X4[4,3]<-0
X5[3,4]<-X5[4,3]<-1

X1[3,5]<-X1[5,3]<-0
X2[3,5]<-X2[5,3]<-1
X3[3,5]<-X3[5,3]<-0
X4[3,5]<-X4[5,3]<-1
X5[3,5]<-X5[5,3]<-0

X1[3,6]<-X1[6,3]<-0
X2[3,6]<-X2[6,3]<-0
X3[3,6]<-X3[6,3]<-1
X4[3,6]<-X4[6,3]<-0
X5[3,6]<-X5[6,3]<-1


# 4. haplotey w/ other haplotypes

X1[4,5]<-X1[5,4]<-0
X2[4,5]<-X2[5,4]<-0
X3[4,5]<-X3[5,4]<-1
X4[4,5]<-X4[5,4]<-0
X5[4,5]<-X5[5,4]<-0

X1[4,6]<-X1[6,4]<-0
X2[4,6]<-X2[6,4]<-1
X3[4,6]<-X3[6,4]<-0
X4[4,6]<-X4[6,4]<-2
X5[4,6]<-X5[6,4]<-2

# 5. haplotey w/ 6. haplotypes

X1[5,6]<-X1[6,5]<-1
X2[5,6]<-X2[6,5]<-0
X3[5,6]<-X3[6,5]<-0
X4[5,6]<-X4[6,5]<-0
X5[5,6]<-X5[6,5]<-0

dmnms<-c("H1","H2","H3","H4","H5","H6")
dimnames(X1)<-list(dmnms,dmnms)
dimnames(X2)<-list(dmnms,dmnms)
dimnames(X3)<-list(dmnms,dmnms)
dimnames(X4)<-list(dmnms,dmnms)
dimnames(X5)<-list(dmnms,dmnms)

# YIJ
print(mu*mu)
print((1-mu)*(mu))
print((1-mu)*(1-mu))

for(i in 1:2){

	p1 <- pp[i]	

	Y[2*i-1,2*i]<-Y[2*i,2*i-1]<-(p1-mu)*(p1-mu)

	for(j in (i+1):3){

		p2 <- pp[j]	

		yij <- (p1-mu)*(p2-mu)

		Y[2*i-1,2*j-1]<-Y[2*i,2*j-1]<-Y[2*i-1,2*j]<-Y[2*i,2*j]<-yij
		Y[2*j-1,2*i-1]<-Y[2*j,2*i-1]<-Y[2*j-1,2*i]<-Y[2*j,2*i]<-yij


	}#end j



}# end i
i<-3
Y[2*i-1,2*i]<-Y[2*i,2*i-1]<-(pp[i]-mu)*(pp[i]-mu)

dmnms<-c("ID1_1","ID1_2","ID2_1","ID2_2","ID3_1","ID3_2")
Y <- data.frame(Y)
rownames(Y) <- dmnms
colnames(Y) <- dmnms


str<-"Basiswerte\nPhaenotypen:\n"
str<-paste(str,"pheno(1) = ", pp[1],"\n",sep="")
str<-paste(str,"pheno(2) = ", pp[2],"\n",sep="")
str<-paste(str,"pheno(3) = ", pp[3],"\n",sep="")
str<-paste(str,"mu       = ", mu,"\n",sep="")
str<-paste(str,"\n Matrix mit paarweisen Vergleichen\n",sep="")
str<-paste(str,"\n Matrix kuenstlich aufgeblaeht da zu jedem haplotypen ein Phaenotyp gehoert\n",sep="")
write(str,file=basiclogfile,append=T)
write.table(Y,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)
######################################################

# print basic values ...

######################################################

str<-"\n\nHaplotypen:\n\n"
str<-paste(str,"H11: ",haps.vec[1],"\n",sep="")
str<-paste(str,"H12: ",haps.vec[2],"\n",sep="")
str<-paste(str,"H21: ",haps.vec[3],"\n",sep="")
str<-paste(str,"H22: ",haps.vec[4],"\n",sep="")
str<-paste(str,"H31: ",haps.vec[5],"\n",sep="")
str<-paste(str,"H32: ",haps.vec[6],"\n",sep="")

str<-paste(str,"\nSharing-Werte:\n\n",sep="")
str<-paste(str,"Die Werte werden als Matrizen dargestellt:\n ",sep="")
str<-paste(str,"Hij: Vergleich i. Haplotype mit j. Haplotyp an der jeweiligen Position\n\n",sep="")

write(str,file=basiclogfile,append=T)

str1<-"\nPosition 1\n"
write(str1,file=basiclogfile,append=T)
write.table(X1,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)
str1<-"\nPosition 2\n"
write(str1,file=basiclogfile,append=T)
write.table(X2,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)
str1<-"\nPosition 3\n"
write(str1,file=basiclogfile,append=T)
write.table(X3,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)
str1<-"\nPosition 4\n"
write(str1,file=basiclogfile,append=T)
write.table(X4,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)
str1<-"\nPosition 5\n"
write(str1,file=basiclogfile,append=T)
write.table(X5,file=basiclogfile,append=T,row.names=T,col.names=T,quote=F)


str1<-"\nAlternative Darstellung\n"
write(str1,file=basiclogfile,append=T)

str1<-haps.vec[1]
str2<-haps.vec[2]
str3<-"55555"
str<-paste("1. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"2. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[1]
str2<-haps.vec[3]
str3<-"01022"
str<-paste("1. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"3. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[1]
str2<-haps.vec[4]
str3<-"00101"
str<-paste("1. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"4. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[1]
str2<-haps.vec[5]
str3<-"44440"
str<-paste("1. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"5. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[1]
str2<-haps.vec[6]
str3<-"10001"
str<-paste("1. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"6. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[2]
str2<-haps.vec[3]
str3<-"01022"
str<-paste("2. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"3. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[2]
str2<-haps.vec[4]
str3<-"00101"
str<-paste("2. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"4. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[2]
str2<-haps.vec[5]
str3<-"44440"
str<-paste("2. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"5. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[2]
str2<-haps.vec[6]
str3<-"10001"
str<-paste("2. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"6. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[3]
str2<-haps.vec[4]
str3<-"10001"
str<-paste("3. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"4. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[3]
str2<-haps.vec[5]
str3<-"01010"
str<-paste("3. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"5. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[3]
str2<-haps.vec[6]
str3<-"00101"
str<-paste("3. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"6. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[4]
str2<-haps.vec[5]
str3<-"00100"
str<-paste("4. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"5. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[4]
str2<-haps.vec[6]
str3<-"01022"
str<-paste("4. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"6. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

str1<-haps.vec[5]
str2<-haps.vec[6]
str3<-"10000"
str<-paste("5. Haplotyp ",str1,"\n",sep="")
str<-paste(str,"6. Haplotyp ",str2,"\n",sep="")
str<-paste(str,"Sharing     ",str3,"\n",sep="")
write(str,file=basiclogfile,append=T)

######################################################

# Test-Statistik

######################################################

M1 <- sum(X1*Y)
M2 <- sum(X2*Y)
M3 <- sum(X3*Y)
M4 <- sum(X4*Y)
M5 <- sum(X5*Y)

calculateD<-function(m){

	n <- ncol(m)

	d <- 0

	for(i in 1:n){

		tmp <- sum(m[i,])

		tmp <- tmp*tmp

		d <- d + tmp

	}	

	d

}

calculateGHK<-function(a,b,d){

	g <- a * a
	h <- d - b;
	k <- g + 2*b - 4*d;

	obj<-list(G=g,H=h,K=k)

	return(obj)

}


calculateAsymp<-function(ax,bx,dx,gx,hx,kx,ay,by,dy,gy,hy,ky,n,M){

	E <- sum(ax*ay)/(n*(n-1))
	
	V = (2*bx*by + 4*hx*hy/(n-2) + kx*ky/((n-2)*(n-3)) - gx*gy/(n*(n-1)))/(n*(n-1));
		
	SD <- sqrt(V)

	t <- (M - E)/SD

	p<-pt(t,n-1,lower.tail=F)
	
	obj<-list(E=E,V=V,SD=SD,t=t,p=p)	

	return(obj)

}


printTests<-function(logfile,i,X,M,ax,bx,dx,hx,gx,kx,E,V,t,p,MP.vec,EP,VP,tP,pP){

	str<-paste("\nAsymptotic Mantel statistic an Position ", i, sep="")	

	str<-paste(str,"\n\n", "M = ", M,sep="")
	str<-paste(str,"\n", "E = ", E,sep="")
	str<-paste(str,"\n", "V = ", V,sep="")
	str<-paste(str,"\n", "t = ", t,sep="")
	str<-paste(str,"\n", "p = ", p,sep="")

	
	str<-paste(str,"\n\n", "Zwischenwerte:\n",sep="")
	str<-paste(str,"\n", "Ax = ", ax,sep="")
	str<-paste(str,"\n", "Bx = ", bx,sep="")
	str<-paste(str,"\n", "Dx = ", dx,sep="")
	str<-paste(str,"\n", "Gx = ", gx,sep="")
	str<-paste(str,"\n", "Hx = ", hx,sep="")
	str<-paste(str,"\n", "Kx = ", kx,sep="")


	write(str,file=logfile,append=T)
	
}

# Y , Phaenotyp

Ay <- sum(Y)
By <- sum(Y*Y)
Dy <- calculateD(Y)
val<-calculateGHK(Ay,By,Dy)
Gy<-val$G
Hy<-val$H
Ky<-val$K

str<-"Werte fuer den asymptotischen Mantel-Test\n\n"
str<-paste(str,"Werte fuer Y:",sep="")
str<-paste(str,"\n\n","Ay = ",Ay,sep="")
str<-paste(str,"\n","By = ",By,sep="")
str<-paste(str,"\n","Dy = ",Dy,sep="")
str<-paste(str,"\n","Gy = ",Gy,sep="")
str<-paste(str,"\n","Hy = ",Hy,sep="")
str<-paste(str,"\n","Ky = ",Ky,sep="")

write(str,file=testlogfile,append=T)

# X1, 1. Position 

A1x <- sum(X1)
B1x <- sum(X1*X1)
D1x <- calculateD(X1)
val<-calculateGHK(A1x,B1x,D1x)
G1x<-val$G
H1x<-val$H
K1x<-val$K

test1<-calculateAsymp(A1x,B1x,D1x,G1x,H1x,K1x,Ay,By,Dy,Gy,Hy,Ky,2*N,M1)
E1<-test1$E
V1<-test1$V
S1<-test1$SD
t1<-test1$t
p1<-test1$p

printTests(testlogfile,1,X1,M1,A1x,B1x,D1x,H1x,G1x,K1x,E1,V1,t1,p1,0,0,0,0)


# X2, 2. Position 

A2x <- sum(X2)
B2x <- sum(X2*X2)
D2x <- calculateD(X2)
val<-calculateGHK(A2x,B2x,D2x)
G2x<-val$G
H2x<-val$H
K2x<-val$K

test1<-calculateAsymp(A2x,B2x,D2x,G2x,H2x,K2x,Ay,By,Dy,Gy,Hy,Ky,2*N,M2)
E2<-test1$E
V2<-test1$V
S2<-test1$SD
t2<-test1$t
p2<-test1$p

printTests(testlogfile,2,X2,M2,A2x,B2x,D2x,H2x,G2x,K2x,E2,V2,t2,p2,0,0,0,0)

# X3, 13 Position 

A3x <- sum(X3)
B3x <- sum(X3*X3)
D3x <- calculateD(X3)
val<-calculateGHK(A3x,B3x,D3x)
G3x<-val$G
H3x<-val$H
K3x<-val$K

test1<-calculateAsymp(A3x,B3x,D3x,G3x,H3x,K3x,Ay,By,Dy,Gy,Hy,Ky,2*N,M3)
E3<-test1$E
V3<-test1$V
S3<-test1$SD
t3<-test1$t
p3<-test1$p

printTests(testlogfile,3,X3,M3,A3x,B3x,D3x,H3x,G3x,K3x,E3,V3,t3,p3,0,0,0,0)


# X4, 4. Position 

A4x <- sum(X4)
B4x <- sum(X4*X4)
D4x <- calculateD(X4)
val<-calculateGHK(A4x,B4x,D4x)
G4x<-val$G
H4x<-val$H
K4x<-val$K

test1<-calculateAsymp(A4x,B4x,D4x,G4x,H4x,K4x,Ay,By,Dy,Gy,Hy,Ky,2*N,M4)
E4<-test1$E
V4<-test1$V
S4<-test1$SD
t4<-test1$t
p4<-test1$p

printTests(testlogfile,4,X4,M4,A4x,B4x,D4x,H4x,G4x,K4x,E4,V4,t4,p4,0,0,0,0)

# X5, 5. Position 

A5x <- sum(X5)
B5x <- sum(X5*X5)
D5x <- calculateD(X5)
val<-calculateGHK(A5x,B5x,D5x)
G5x<-val$G
H5x<-val$H
K5x<-val$K

test1<-calculateAsymp(A5x,B5x,D5x,G5x,H5x,K5x,Ay,By,Dy,Gy,Hy,Ky,2*N,M5)
E5<-test1$E
V5<-test1$V
S5<-test1$SD
t5<-test1$t
p5<-test1$p

printTests(testlogfile,5,X5,M5,A5x,B5x,D5x,H5x,G5x,K5x,E5,V5,t5,p5,0,0,0,0)





