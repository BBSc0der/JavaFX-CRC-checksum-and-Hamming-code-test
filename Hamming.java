/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

/**
 *
 * @author Bolek
 */
public class Hamming extends CodeBase
{
	@Override
	int[] encode()
	{
		int n = data.length;
		int i=0, nadmiar=0, suma=0;
		while (i<n)						// liczenie długości kodu
		{
			if (Math.pow(2, nadmiar)-1==suma) nadmiar++;	// potęga 2 - bit redundantny
			else i++;					// bit danych
			suma++;
		}
		code = new int[suma];
		type = new int[suma];
		
		long maska=0;
		nadmiar=0;
		int d=0;
		i=0;
		suma=0;
		while (i<n)						// przepisz dane i wylicz bity kontrolne
		{
			if (Math.pow(2,nadmiar)-1==suma) nadmiar++;
			else
			{
				code[suma]=data[i];
				if (data[i]==1) maska^=suma+1;
				i++;
			}
			suma++;
		}
		
		nadmiar=0;		// tutaj nadmiar pełni też rolę numeru bitu w masce
		for (i=0; i<suma; i++)	// zapisz bity kontrolne
		{
			if (Math.pow(2,nadmiar)-1==i)
			{
				if ((maska&((long)1<<nadmiar))==0) code[i]=0;
				else code[i]=1;
				nadmiar++;
			}
		}

		return code;
	}

	@Override
	int[] decode()
	{
		int n=code.length;
		int d=0;
		int nadmiar=0;
		for (int i=0; i<n; i++)		// liczenie długości danych
		{
			if (Math.pow(2, nadmiar)-1!=i) d++;
			else nadmiar++;
		}
		
		data = new int[d];
		d=0;
		nadmiar=0;
		
		for (int i=0; i<n; i++)		// przepisanie danych
		{
			if (Math.pow(2, nadmiar)-1!=i)	// bit danych
			{
				data[d]=code[i];
				d++;
			}
			else nadmiar++;
		}
		
		return data;
	}

	@Override
	void fix()
	{
		int n=code.length;
		int d=0;
		int nadmiar=0;
		for (int i=0; i<n; i++)		// liczenie długości danych
		{
			if (Math.pow(2, nadmiar)-1!=i) d++;
			else nadmiar++;
		}
		
		data = new int[d];
		
		int maska=0;
		d=0;
		nadmiar=0;
		
		for (int i=0; i<n; i++)
		{
			// kontrola poprawności
			if (code[i]==1) maska^=i+1;
			
			// określanie typu bitów
			if (Math.pow(2, nadmiar)-1!=i)		// bit danych
			{
				d++;
				type[i]=0;			// poprawny (jak na razie) bit danych
			}
			else
			{
				type[i]=3;				// poprawny (jak na razie) bit redundantny
				nadmiar++;
			}
		}
		
		if (maska!=0)					// wykryto błąd
		{
			errors++;
			int numer=maska-1;			// numeracja bitów od 1, tablicy - od 0

                        possition = numer;
                        
			if (numer<code.length)
			{
				if (type[numer]==0) type[numer]=1;	// korygujemy bit danych
				else if (type[numer]==3) type[numer]=4;	// korygujemy bit redundantny

				if (code[numer]==1) code[numer]=0;
				else code[numer]=1;
			}
		}
	}
}

