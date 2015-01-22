/*me : Swati Jagdale
Programming assignment 1
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h> 

int main()
{
	char ch, file_name[25];
	FILE *fp; FILE *fpResult; char output[1000];  int outputIndex = 0;
	fpResult = fopen("res.txt", "w");
	char  codeword[200]; 
	printf("Enter the name of file-->\n");
	gets(file_name);
        
	//Reading input from a file
	fp = fopen(file_name,"r"); // read mode
	char  bits[500] ;
	if( fp == NULL )
	{
		perror("Error while opening the file.\n");
		exit(EXIT_FAILURE);
	}

	printf("The contents of %s file are :\n", file_name);

	//Get the cntents os file in a char array
	int i = 0; 
	int l = 0;
	while( ( ch = fgetc(fp) ) != EOF ){
		bits[i] = ch;
		i++;
	}
	printf("bit array \n");
	int j;
	for(j = 0 ; j < i; j++){
		printf("%c",bits[j]);
	}
        //Char array processing, taking codeword into one array codeword
	int isNumber = 0;
	int k ;
	for(k = 0; k < i ; k++){
		if(bits[k] =='\n'){
			isNumber = 0;
		}
		if(bits[k] != ' ' && bits[k-1] !=' ' && bits[k] != '\n' && isNumber==0){
			codeword[l] = bits[k];
			l++;
		}

		if(bits[k] == ' '){
			int c = k;
			char totalBits[5] = "";
			int tb = 0;
        //Calculate number of bits for the codeword
			while(bits[c] != '\n'){
				if(bits[c+1] != ' ' && bits[c+1] !='\n'){
					totalBits[tb]= bits[c+1];
					tb++;
					isNumber = 1;

				}
				c++;
			}
			int firstDigit = totalBits[0] - '0';
			int secondDigit = totalBits[1] - '0';

			int total;
			if(secondDigit < 0)
				total =firstDigit;
			else
				total = firstDigit*10 + secondDigit;
			int noOfBits = total;
			int index;
         //Generating padded code from Codeword
			if(l < noOfBits){
				int toAdd = noOfBits - l;
				int index1;
				for(index1 = 0; index1 < toAdd ; index1++){
					output[outputIndex]='0';
					outputIndex++;
				}
			}
			int index2;
			for(index2 = 0; index2 < l ; index2++){
				output[outputIndex]=codeword[index2];
				outputIndex++;
			}
			l = 0;
		}
	}

	int z; int times = 0; int totalOutput = outputIndex;
	int writeBytes; int byteRemainder = totalOutput % 8;
	if(byteRemainder != 0)
		writeBytes = totalOutput+ (8-byteRemainder);
	else{
		writeBytes = totalOutput;
	}
	int passes = outputIndex / 32;
	int remainder = outputIndex % 32;
	if(remainder != 0)
		times = passes +1;
	else{
		times = passes;
	}
	for(z = outputIndex; z < 32*times; z++){
		output[z] = '0';
		outputIndex++;
	}

        //get the byte version of the padded codeword
	printf("Output is -->");
	int o;
	int start = 0; int number; int noOfByte = 0; int checkBit;
	for(number = 1; number <= times ; number++){
		unsigned char a;
		a = 0x0;
		int count = 0;
		for(o =start ; o <32*number; o++){
			if(o == writeBytes)
				return;
			count++;
			printf("%c",output[o]);
			if(output[o] =='1'){
				a = a | 1;
			}
			else{
				a = a | 0;
			}

			checkBit = (8 * noOfByte)  ;
			if((count % 8 != 0)|| (count % 8 == 0 && output[checkBit] == '0')){
				a = a<<1;
			}else if(count % 8 == 0 &&  output[checkBit] == '1'){}
			if(count % 8 == 0){
				if(output[checkBit] != '1')
					a= a>>1;
				noOfByte++;
				fwrite( &a, 1,1, fpResult );
			}
		}
		start = 32*number;
	}
	fclose(fpResult);
	fclose(fp);
	return 0;
}
