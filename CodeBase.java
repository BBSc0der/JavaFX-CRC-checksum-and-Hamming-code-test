/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

import java.util.Random;

abstract class CodeBase
{
	protected int[] data;
	protected int[] code;
	protected int[] type;	// 0 - poprawny bit danych, 1 - przekłamany bit danych, 2 - niepewny bit danych, 3 - poprawny bit redundantny, 4 - przekłamany bit redundantny, 5 - niepewny bit redundantny
	protected int errors=0;
        protected int possition=0;
	
	public void setData(int[] dane)
	{
		this.data = new int[dane.length];
		System.arraycopy(dane, 0, this.data, 0, dane.length);
		errors=0;
	}

	
	public void setData(String str)
	{
		int n = str.length();
		data = new int[n];
		for (int i=0; i<n; i++)
		{
			if (str.charAt(i)=='1') data[i] = 1;
			else data[i] = 0;
		}
		errors=0;
	}

	public void setCode(int[] kod)
	{
		this.code = new int[kod.length];
		this.type = new int[kod.length];
		System.arraycopy(kod, 0, this.code, 0, kod.length);
	}
	

	
	public void setCode(String str)
	{
		int n = str.length();
		code = new int[n];
		this.type = new int[n];
		for (int i=0; i<n; i++)
		{
			if (str.charAt(i)=='1') code[i] = 1;
			else code[i] = 0;
		}
		errors=0;
	}
	
	abstract int[] encode();
	abstract int[] decode();
	
	public int getDataBitsNumber()
	{
		return data.length;
	}
	
	public int getControlBitsNumber()
	{
		return code.length-data.length;
	}
	
	public int getDetectedErrorsNumber()
	{
		return errors;
	}
	
	public int getFixedErrorsNumber()
	{
		int b=0;
		for (int i=0; i<type.length; i++)
		{
			if (type[i]==1 || type[i]==4) b++;
		}
		return b;
	}
	

	
	public int[] getBitTypes()
	{
		return type;
	}
	
	abstract void fix();
	
	public void interfere(int n)
	{
		int l = code.length;
		Random generator = new Random();
		if (n>l) n=l;
		int pozycja;
		int zaklocone=0;
		for (int i=0; i<l; i++) type[i]=0;
		while (zaklocone<n)
		{
			pozycja=generator.nextInt(l);
			if (type[pozycja]==0)
			{
				if (code[pozycja]==1) code[pozycja]=0;
				else code[pozycja]=1;
				type[pozycja]=1;
				zaklocone++;
			}
		}
	}
	
	public void negate(int pozycja)
	{
		if (pozycja<code.length)
		{
			if (code[pozycja]==1) code[pozycja]=0;
			else code[pozycja]=1;
			if (type[pozycja]==1) type[pozycja]=0;
			else type[pozycja]=1;
		}
	}

    public int getPossition() {
        return possition;
    }
    public void resetPossition(){
        possition = 0;
    }
    public int[] getCode() {
        return code;
    }
    
}

