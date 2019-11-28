package com.nojava;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(Arrays.toString(args) );
        String[]  a = {"sdf","dfsd","dsfsfs","rewr"};
        System.out.println(Arrays.toString(a) );
        for(String aaa:a){
            System.out.println(aaa.length());
        }
    }
}
