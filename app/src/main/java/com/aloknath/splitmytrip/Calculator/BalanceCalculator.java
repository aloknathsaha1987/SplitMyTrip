package com.aloknath.splitmytrip.Calculator;

import android.util.Log;

import com.aloknath.splitmytrip.Objects.Person;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ALOKNATH on 3/12/2015.
 */
public class BalanceCalculator implements  java.io.Serializable{

    public static List <HashMap< String , Object >> calculate ( List<Person> persons
            , double tolerance ) {

        tolerance += 0.009;

        List<HashMap<String,Object>> results = new LinkedList<>() ;

        Iterator<Person> iterator = persons.iterator();

        while(iterator.hasNext()){
            Person person = iterator.next();
            Log.i("Value of person's Balance ", String.valueOf(person.getBalance()) + " His Name: " + person.getName());
            if ( Math.abs(person.getBalance()) <= tolerance ){
                iterator.remove() ;
            }
        }

        if ( persons.size() == 1) {
            return results;
        }

        List<HashMap<String,Object>> values = basicDebts ( persons,tolerance ) ;
        results.addAll(values) ;
        Collections.sort(results, new DebtComparator()) ;
        return results ;
    }

    static class DebtComparator implements Comparator< HashMap < String , Object >> {

        @Override
        public int compare(HashMap<String, Object> stringObjectHashMap, HashMap<String, Object> stringObjectHashMap2) {
             return ((Person)stringObjectHashMap.get("from")).compareTo(((Person) stringObjectHashMap2.get("from")));
        }

    }

    private static List <HashMap<String,Object>> basicDebts (List<Person> persons, double tolerance ) {

        List <HashMap < String , Object > > debts = new LinkedList <>() ;

        int resolvedMembers = 0;

        while (resolvedMembers != persons.size ()) {

            // transaction is from lowest balance to highest balance
            Collections.sort(persons) ;

            for (Person person: persons){
                Log.i("Persons Sorted: ", person.toString());
            }

            Person sender = persons.get(0);
            Person recipient = persons.get(persons.size() - 1) ;

            double senderShouldSend = Math . abs ( sender.getBalance() ) ;
            double recipientShouldReceive = Math . abs (recipient.getBalance()) ;

            double amount ;

            if ( senderShouldSend > recipientShouldReceive ) {

                 amount = recipientShouldReceive ;

            } else {
                 amount = senderShouldSend ;
            }
            Log.i("Sender Details from balance calculator before calc: ", sender.toString());
            Log.i("Recipient Details from balance calculator before calc: ", recipient.toString());
            sender.setBalance(sender.getBalance() + amount);
            recipient.setBalance(recipient.getBalance() - amount);
            Log.i("Sender Details from balance calculator: ", sender.toString());
            Log.i("Recipient Details from balance calculator: ", recipient.toString());

            HashMap < String , Object > values = new HashMap <>() ;

            values.put("from", sender) ;
            values.put("amount", amount) ;
            values.put("to", recipient) ;

            debts.add(values);

            // delete members who are settled
            senderShouldSend = Math . abs ( sender.getBalance())  ;
            recipientShouldReceive = Math . abs ( recipient.getBalance() ) ;

            if ( senderShouldSend <= tolerance ) {
                resolvedMembers++;
            }else if ( recipientShouldReceive <= tolerance ) {
                resolvedMembers++;
            }
        }

        // limit transactions by tolerance

        Iterator < HashMap < String , Object > > iterator = debts . iterator () ;
        while ( iterator . hasNext () ) {
            HashMap < String , Object > debt = iterator . next () ;
            //Log.i("The Debt Iterator's values: ", String.valueOf(debt.get("amount")) );
            if ((Double)debt.get("amount") <= tolerance )
               iterator . remove () ;
        }
        return debts;
    }

 }
