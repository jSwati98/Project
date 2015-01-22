#include <stdio.h>
#include<stdlib.h>
#include<math.h>
#define PI 3.14159265358979323846
//void subGroup(int a[16][16]);
float dct(int u,int v,int mat[8][8]);
void calDCT(int mat[8][8]);
//void makeMacroblock(int original[300000][300000],int x,  int y);
float quantcoeff[8][8],qt[8][8];
float qScale;
int main(int argc, char *argv[]) {
int i;
FILE *fp, *ifp;
int c;
int *p;
int values [300000];
int a[16][16];
int j= 0;
int row, col, maxValue;
char str[3];

//int quantcoeff[8][8],qt[8][8];
FILE *f1p, *ofp;
//f1p = fopen("output.txt", "w");
//ofp=fopen("quantfile.txt","r");


for (i = 1; i < argc; i++) {
  if( i==1){
  fp = fopen(argv[i], "r");

    if (fp == NULL) {
//        fprint("cat: can't open %s\n", argv[i]);
        continue;
    }

//int row, col, maxValue;
//	char str[3];
//	ifp = fopen (argv[1], "r");
    fscanf(fp, "%s", str); // Reading P5 from test0.pgm
    fscanf(fp, "%d %d", &row, &col); // Reading rows and columns from test0.pgm
    fscanf(fp, "%d", &maxValue); // Reading MaxValue from test0.pgm
	printf("First line: %s\n", str);
	printf("Rows = %d, Cols = %d, MaxValue = %d\n", row, col, maxValue);

    while ((c =fgetc(fp)) != EOF) {
//	putchar(c);
	unsigned char ch = (unsigned char) c;

//	 printf("%d    ", c);

if(ch == '\n'){
j = 0;
}else{
values[j] = c;
j++;
}

//fwrite(&ch,1, sizeof(char),f1p);
//fwrite(&c,1, sizeof(int),f1p);

}

    fclose(fp);
}
else if (i ==2){
//f1p = fopen("output.txt", "w");
ofp=fopen(argv[2],"r");
int qCoef;
int j1 = 0;
printf("qUANT MATRIX ************");
for(qCoef=0;qCoef<8;qCoef++)
        {
                for(j1=0;j1<8;j1++)
                {
                        
			fscanf(ofp,"%f",&qt[qCoef][j1]);
			printf("%f ",  qt[qCoef][j1]);
///			if(!fscanf(ofp,"%f",&qt[qCoef][j1]))
 //                                       break;
                }
printf("\n");
        }


}else if(i == 3){

qScale = atof(argv[3]);
}
else if(i== 4){
f1p = fopen(argv[4], "w");
//ofp=fopen("quantfile.txt","r");

}
}

printf("xsize ------%d",row);
printf("ysize ------%d",col);
/*int qCoef;
int j1 = 0;
for(qCoef=0;qCoef<8;qCoef++)
        {
                for(j1=0;j1<8;j1++)
                {
                        if(!fscanf(ofp,"%f",&qt[qCoef][j1]))
                                        break;
                }
        }*/

int k;
int l;
int original[row][col];
//p = &original;
int index= 0;
printf("Original Matrix--->\n");
for(k = 0; k < row; k++){
for(l = 0; l < col; l++){
//printf("%d ",values[k]);
original[k][l] = values[index];
index++;
printf("%d ",original[k][l]);
}
printf("\n");

}
int one[8][8];
int two[8][8];
int three[8][8];
int four[8][8];

for(k = 0; k < 16; k++){
for(l = 0; l < 16; l++){


//printf("ro = %d co = %d",ro, co);
//printf("\n");
///a[i][j] = original[ro][co];
//printf("%d ",original[k][l]);
//j++;
}
//printf("\n");

}

printf("rows %d col %d", row, col);
printf("\n");
int m, n;
//col=32;
//row =32;
for(m = 1; m <= col/16; m++){

for(n = 1 ; n <= row/16; n++){
printf("m ----- %d,n---- %d",(m-1)*16,(n-1)*16);
printf("\n");

int ro, co;
int x = m;
int y = n;
int i, j= 0;
for(ro = (x-1)*16; ro < x*16; ro++){
for(co = (y-1)*16; co < y*16; co++){
//for(ro = 0; ro < 16; ro++){
//for(co = 0; co < 16; co++){


//printf("ro = %d co = %d",i, j);
//printf("\n");
a[i][j] = original[ro][co];
printf("%d ",original[ro][co]);
j++;
}

i++;
j = 0;
printf("\n");
}

//to check a's values
printf("A is------------------>\n");
for(ro = 0; ro < 16; ro++){
for(co = 0; co < 16; co++){

printf("%d ",a[ro][co]);
}

printf("\n");

}

int row1;
int col1;

for(row1 = 0; row1 < 8; row1++){
for(col1 = 0; col1 < 8; col1++){

one[row1][col1] = a[row1][col1];
two[row1][col1] = a[row1][col1+8];
three[row1][col1] = a[row1+8][col1];
four[row1][col1] = a[row1+8][col1+8];

}

printf("\n");
}
int row2;
int col2 ;


int itr;

for(itr = 0; itr < 4; itr++){
printf("m = %d,n = %d \n",m,n);
if(itr == 0)
printf("%d %d \n",(n-1)*16,(m-1)*16);
else if(itr == 1)
printf("%d %d \n",((n-1)*16)+8,(m-1)*16+0);

//printf("8, 16\n");
else if(itr == 2)
//printf("8, 8\n");
printf("%d %d \n",(n-1)*16,(m-1)*16+8);

else
//printf("8, 16\n");
printf("%d %d \n",(n-1)*16+8,(m-1)*16+8);


for(row2 = 0; row2 < 8; row2++){
for(col2 = 0; col2 < 8; col2++){
if(itr == 0)
printf("%d  ",one[row2][col2]);
else if(itr == 1)
printf("%d  ",two[row2][col2]);
else if(itr == 2)
printf("%d  ",three[row2][col2]);
else
printf("%d  ",four[row2][col2]);


}
printf("\n");
}

printf("\n");
if(itr == 0)
calDCT(one);
else if(itr == 1)
calDCT(two);
else if(itr == 2)
calDCT(three);
else
calDCT(four);

printf("\n");
}

}
}

fclose(f1p);
printf("\n");
return 0;
}


/*
Convert an RGB macro block ( 16x16 ) to 4:2:0 YCbCr sample blocks
(six 8x8 blocks).
*/
float dct(int u, int v, int mat[8][8])
        {
        int i, j;
	float matvalue=0.0;
                for(i=0;i<8;i++)
                {
                        for(j=0;j<8;j++)
                        {
                                matvalue+=mat[i][j]*cos((float)((2*i)+1)*u*PI/16.0)*cos((float)((2*j)+1)*v*PI/16.0);
                }
                }
            return(matvalue);
        }


void calDCT(int mat[8][8]){
int u, v, Cu, Cv, i , j;
float  dctValue = 0.0;
double F[8][8];
for(u=0;u<8;u++)
	{
	 //   printf("\n");
		for(v=0;v<8;v++)
		{
		  //  printf("\n");
			dctValue=dct(u,v,mat)/4;
		    //printf("%f",dctvalue);
			Cu=1;
			Cv=1;
			if(u==0)
				Cu=(1/sqrt(2));
			if(v==0)
				Cv=(1/sqrt(2));

		   // {   
				
		    //printf("%f",dctvalue);
		//		F[u][v]=((1/sqrt(2))/2)*((1/sqrt(2)/2))*dctvalue;
		   // printf(" foo value is  %f",F[u][v]);}   
		   // 	printf("%f   ",dctvalue);
			 F[u][v]=Cu*Cv*dctValue;
		}
	}
	printf("Dct matrix is\n");
	for(i=0;i<8;i++)
	{
		 	for(j=0;j<8;j++)
		{
			printf("%f  ",F[i][j]);
		}
			printf("\n");
	}
/*int qCoef;
int j1 = 0;
for(qCoef=0;qCoef<8;qCoef++)
        {
                for(j1=0;j1<8;j1++)
                {
                        if(!fscanf(ofp,"%f",&qt[qCoef][j1]))
                                        break;
                }
        }*/

printf("\n After Quantization--> \n");
        for(i=0;i<8;i++)
        {
                for(j=0;j<8;j++)
                {
                        quantcoeff[i][j]=round(F[i][j]/qt[i][j]);
                               // printf("%f  ",quantcoeff[i][j]);
printf("%f  ",qt[i][j]);
//printf("%f  ",quantcoeff[i][j]);

                }
printf("\n");
        }

}



/*printf("rows %d col %d", row, col);
printf("\n");
int m, n;
for(m = 1; m <= row/16; m++){

for(n = 1 ; n <= col/16; n++){
printf("m ----- %d,n---- %d",(m-1)*16,(n-1)*16);
printf("\n");
//makeMacroblock(original,m,n);
int a[16][16];
int ro, co;
int x = m;
int y = n;
int i, j= 0;
for(ro = (x-1)*16; ro < x*16; ro++){
for(co = (y-1)*16; co < y*16; co++){

//printf("ro = %d co = %d",ro, co);
//printf("\n");
a[i][j] = original[ro][co];
printf("%d ",original[ro][co]);
j++;
}

i++;
//j = 0;
int row1;
int col1;

for(row1 = 0; row1 < 8; row1++){
for(col1 = 0; col1 < 8; col1++){

//printf("%d ",one[row1][col1]);
one[row1][col1] = a[row1][col1];
two[row1][col1] = a[row1][col1+8];
three[row1][col1] = a[row1+8][col1];
four[row1][col1] = a[row1+8][col1+8];

}

printf("\n");
}
int row2;
int col2 ;


int itr;

for(itr = 0; itr < 4; itr++){
if(itr == 0)
printf("0, 8\n");
else if(itr == 1)
printf("8,16\n");
else if(itr == 2)
printf("8,16\n");
else
printf("16, 16\n");

for(row2 = 0; row2 < 8; row2++){
for(col2 = 0; col2 < 8; col2++){
if(itr == 0)
printf("%d  ",one[row2][col2]);
else if(itr == 1)
printf("%d  ",two[row2][col2]);
else if(itr == 2)
printf("%d  ",three[row2][col2]);
else
printf("%d  ",four[row2][col2]);


}
printf("\n");
}
printf("\n");
if(itr == 0)
calDCT(one);
else if(itr == 1)
calDCT(two);
else if(itr == 2)
calDCT(three);
else
calDCT(four);
}

printf("\n");
}
}
}

fclose(f1p);
printf("\n");
return 0;
}


/*
Convert an RGB macro block ( 16x16 ) to 4:2:0 YCbCr sample blocks
(six 8x8 blocks).

float dct(int u, int v, int mat[8][8])
        {
        int i, j;
	float matvalue=0.0;
                for(i=0;i<8;i++)
                {
                        for(j=0;j<8;j++)
                        {
                                matvalue+=mat[i][j]*cos((float)((2*i)+1)*u*PI/16.0)*cos((float)((2*j)+1)*v*PI/16.0);
                }
                }
            return(matvalue);
        }


void calDCT(int mat[8][8]){
int u, v, Cu, Cv, i , j;
float  dctValue = 0.0;
double F[8][8];
for(u=0;u<8;u++)
	{
	 //   printf("\n");
		for(v=0;v<8;v++)
		{
		  //  printf("\n");
			dctValue=dct(u,v,mat)/4;
		    //printf("%f",dctvalue);
			Cu=1;
			Cv=1;
			if(u==0)
				Cu=(1/sqrt(2));
			if(v==0)
				Cv=(1/sqrt(2));

		   // {   
				
		    //printf("%f",dctvalue);
		//		F[u][v]=((1/sqrt(2))/2)*((1/sqrt(2)/2))*dctvalue;
		   // printf(" foo value is  %f",F[u][v]);}   
		   // 	printf("%f   ",dctvalue);
			 F[u][v]=Cu*Cv*dctValue;
		}
	}
	printf("Dct matrix is\n");
	for(i=0;i<8;i++)
	{
		 	for(j=0;j<8;j++)
		{
			printf("%f  ",F[i][j]);
		}
			printf("\n");
	}*/
/*int qCoef;
int j1 = 0;
for(qCoef=0;qCoef<8;qCoef++)
        {
                for(j1=0;j1<8;j1++)
                {
                        if(!fscanf(ofp,"%f",&qt[qCoef][j1]))
                                        break;
                }
   
     }*/
/*
printf("\n After Quantization--> \n");
        for(i=0;i<8;i++)
        {
                for(j=0;j<8;j++)
                {
                        quantcoeff[i][j]=round(F[i][j]/qt[i][j]);
                                printf("%f  ",quantcoeff[i][j]);
                }
printf("\n");
        }

}
*/

