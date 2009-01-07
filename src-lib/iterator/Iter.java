/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package iterator;


import java.util.*;

import lib.Action;
import lib.ActionKeyValue;

public class Iter<T> implements Iterable<T>, Iterator<T>
{
    // First part : the static function library.
    // Often with both Iterator<? extends T> and Iterable<? extends T>
    
    public static <T> Iterator<T> singleton(T item)
    { return new SingletonIterator<T>(item) ; }

    public static <T> Iterator<T> nullIterator()
    { return new NullIterator<T>() ; }
    
    public static <T> Set<T> toSet(Iterable<? extends T> stream) { return toSet(stream.iterator()); }

    public static <T> Set<T> toSet(Iterator<? extends T> stream)
    {
        Accumulate<T,Set<T>> action = new Accumulate<T,Set<T>>()
        {
            private Set<T> acc = null ;
            public void accumulate(T item)  { acc.add(item) ; }
            public Set<T> get()             { return acc ; }
            public void start()             { acc = new HashSet<T>() ; }
            public void finish()            {}
        } ;
        return reduce(stream, action) ;
    }

    public static <T> List<T> toList(Iterable<? extends T> stream)
    { return toList(stream.iterator()) ; }

    public static <T> List<T> toList(Iterator<? extends T> stream)
    {
        Accumulate<T,List<T>> action = new Accumulate<T,List<T>>()
        {
            private List<T> acc = null ;
            public void accumulate(T item)  { acc.add(item) ; }
            public List<T> get()            { return acc ; }
            public void start()             { acc = new ArrayList<T>() ; }
            public void finish()            {}
        } ;
        return reduce(stream, action) ;
    }

    // Note fold-left and fold-right
    // http://en.wikipedia.org/wiki/Fold_%28higher-order_function%29
    // This reduce is fold-right (take first element, apply to rest of list)
    // which copes with infinite lists.
    // Fold-left starts combining from the list tail.
    
    public static <T, R> R reduce(Iterable<? extends T> stream, Accumulate<T, R> aggregator)
    { return reduce(stream.iterator(), aggregator) ; }

    public static <T, R> R reduce(Iterator<? extends T> stream, Accumulate<T, R> aggregator)
    {
        aggregator.start();
        for ( ; stream.hasNext() ; )
        {
            T item = stream.next(); 
            aggregator.accumulate(item) ;
        }
        aggregator.finish();
        return aggregator.get();
    }

    // map without the results - do immediately.
    
    public static <T> void apply(Iterable<? extends T> stream, Action<T> action)
    { apply(stream.iterator(), action) ; }

    public static <T> void apply(Iterator<? extends T> stream, Action<T> action)
    {
        for ( ; stream.hasNext() ; )
        {
            T item = stream.next(); 
            action.apply(item) ;
        }
    }
    
    // -- Map specific apply.  No results - do immediately.
    
    public static <K, V> void apply(Map<K, V> map, ActionKeyValue<K, V> action)
    {
        for ( Map.Entry<K,V> entry : map.entrySet() )
            action.apply(entry.getKey(), entry.getValue()) ;
    }
    
    // ---- Filter
    
    public static <T> Iterator<T> filter(Iterable<? extends T> stream, Filter<T> filter)
    { return filter(stream.iterator(), filter) ; }

    public static <T> Iterator<T> filter(final Iterator<? extends T> stream, final Filter<T> filter)
    {
        final Iterator<T> iter = new Iterator<T>(){
            
            boolean finished = false ; 
            T slot ;
            
            public boolean hasNext()
            {
                if ( finished )
                    return false ; 
                while ( slot == null )
                {
                    if ( ! stream.hasNext() )
                    { 
                        finished = true ;
                        break ;
                    }
                    T nextItem = stream.next() ;
                    if ( filter.accept(nextItem) )
                    { 
                        slot = nextItem ;
                        break ;
                    }
                }
                return slot != null ;
            }
    
            public T next()
            {
                if ( hasNext() )
                {
                    T returnValue = slot ;
                    slot = null ;
                    return returnValue ;
                }
                throw new NoSuchElementException("filter.next") ;
            }
    
            public void remove() { throw new UnsupportedOperationException("filter.remove") ; }
        } ;
        
        return iter ;
    }
    
    
    private static class InvertedFilter<T> implements Filter<T>
    {
        public static <T> Filter<T> invert(Filter<T> filter) { return new InvertedFilter<T>(filter) ; }
        private Filter<T> baseFilter ;
        private InvertedFilter(Filter<T> baseFilter) { this.baseFilter = baseFilter ; }
        
        public boolean accept(T item)
        {
            return ! baseFilter.accept(item) ;
        }
    }
    
    public static <T> Iterator<T> notFilter(Iterable<? extends T> stream,
                                         Filter<T> filter)
    { return notFilter(stream.iterator(), filter) ; }
    
    public static <T> Iterator<T> notFilter(final Iterator<? extends T> stream, final Filter<T> filter)
    {
        Filter<T> flippedFilter = InvertedFilter.invert(filter) ;
        return filter(stream, flippedFilter) ;
    }
    
    // Filter-related
    
    /** Return true if every element of stream passes the filter (reads the stream) */
    public static <T> boolean every(Iterable<? extends T> stream, Filter<T> filter)
    { 
        for ( T item : stream )
            if ( ! filter.accept(item) ) 
                return false ;
        return true ;
    }

    /** Return true if every element of stream passes the filter (reads the stream until the first element not passing the filter) */
    public static <T> boolean every(Iterator<? extends T> stream, Filter<T> filter)
    { 
        for ( ; stream.hasNext() ; )
        {
            T item = stream.next();
            if ( ! filter.accept(item) ) 
                return false ;
        }
        return true ;
    }

    /** Return true if every element of stream passes the filter (reads the stream) */
    public static <T> boolean some(Iterable<? extends T> stream, Filter<T> filter)
    { 
        for ( T item : stream )
            if ( filter.accept(item) ) 
                return true ;
        return false ;
    }

    /** Return true if one or more elements of stream passes the filter (reads the stream to first element passing the filter) */
    public static <T> boolean some(Iterator<? extends T> stream, Filter<T> filter)
    { 
        for ( ; stream.hasNext() ; )
        {
            T item = stream.next();
            if ( filter.accept(item) ) 
                return true ;
        }
        return false ;
    }

    
    // ---- Map

    public static <T, R> Iterator<R> map(Iterable<? extends T> stream, Transform<T, R> converter)
    { return map(stream.iterator(), converter) ; }

    public static <T, R> Iterator<R> map(final Iterator<? extends T> stream, final Transform<T, R> converter)
    {
        final Iterator<R> iter = new Iterator<R>(){
            public boolean hasNext()
            {
                return stream.hasNext() ;
            }
    
            public R next()
            {
                return converter.convert(stream.next()) ;
            }
    
            public void remove() { throw new UnsupportedOperationException("map.remove") ; }
        } ;
        return iter ;
    }
    
    public static <T> Iterator<T> append(Iterable<T> iter1, Iterable<T> iter2)
    {
        return Iterator2.create(iterator(iter1), iterator(iter2));
    }

    public static <T> Iterator<T> append(Iterator<? extends T> iter1, Iterator<? extends T> iter2)
    { return Iterator2.create(iter1, iter2); }

    private static <T> Iterator<T> iterator(Iterable<T> iter) { return (iter==null) ? null : iter.iterator() ; }
    
//    public static <T>
//    Iter<T> append(Iterator<? extends T> iterator, Iterator<? extends T> iter)
//    {
//        return new Iter<T>(new Iterator2<T>(iterator, iter)) ;
//    }
//
    public static <T> Iterator<T> distinct(Iterable<T> iter)
    {
        return distinct(iter.iterator()) ;
    }

    public static <T> Iterator<T> distinct(Iterator<T> iter)
    {
        return filter(iter, new FilterUnique<T>()) ;
    }
    
    public static <T> Iterator<T> removeNulls(Iterable<T> iter)
    {
        return filter(iter, new FilterOutNulls<T>()) ;
    }
    
    public static <T> Iterator<T> removeNulls(Iterator<T> iter)
    {
        return filter(iter, new FilterOutNulls<T>()) ;
    }
    
    @SuppressWarnings({"unchecked"})
    public static <T> Iterator<T> convert(Iterator<?> iterator) { return (Iterator<T>)iterator ; }

    // ---- String related helpers
    
    public static <T> String asString(Iterable<T> stream)
    { return reduce(stream, new AccString<T>()) ; }

    public static <T> String asString(Iterator<T> stream)
    { return reduce(stream, new AccString<T>()) ; }

    public static <T> String asString(Iter<T> stream)
    { return reduce(stream.iterator(), new AccString<T>()) ; }

    public static <T> String asString(Iterable<T> stream, String sep)
    { return reduce(stream, new AccString<T>(sep)) ; }

    public static <T> String asString(Iterator<T> stream, String sep)
    { return reduce(stream, new AccString<T>(sep)) ; }

    public static <T> String asString(Iter<T> stream, String sep)
    { return reduce(stream.iterator(), new AccString<T>(sep)) ; }

    public static <T> void close(Iterator<T> iter)
    {
        if ( iter instanceof ClosableIterator )
            ((ClosableIterator)iter).close() ;
    }

    public static <T> Iterator<T> debug(Iterator<T> stream)
    { 
        Transform<T,T> x = new Transform<T, T>()
        {
            @Override
            public T convert(T item)
            { 
                System.out.println(item) ;
                return item ;
            }
        } ;
        return map(stream, x) ;
    }
    //----
    // Iter class part
    // And ....
    // Could merge in concatenated iterators - if used a lot there is reducable cost.
    // Just putting in a slot is free (?) because objects of one or two slots have
    // the same memory allocation.
    // And .. be an iterator framework for extension
    
    // Or dynamically with a subclass and a static constructor
    // List<Iterator> concatenated = null ; 
    
    public static <T> Iter<T> iter(Iter<T> iter)
    { return iter ; }
    
    public static <T> Iter<T> iter(Collection<T> collection)
    {
        return Iter.iter(collection.iterator()) ;
    }
    
    public static <T> Iter<T> iter(Iterator<T> iterator)
    { 
        if ( iterator instanceof Iter )
            return (Iter<T>)iterator ;
        return new Iter<T>(iterator) ;
    }
    
    public static <T> Iter<T> iter(Iterable<T> iterable)
    { 
        if ( iterable instanceof Iter )
            return (Iter<T>)iterable ;
        return new Iter<T>(iterable.iterator()) ;
    }
    
    public static <T> Iter<T> concat(Iter<T> iter1, Iter<T>iter2)
    { 
        if ( iter1 == null )
            return iter2 ;
        if ( iter2 == null )
            return iter1 ;
        return iter1.append(iter2) ;
    }

    // The class.
    // Consider merging in a 
    
    private Iterator<T> iterator ;
    private Iter(Iterator<T> iterator) { this.iterator = iterator ; }
    
    public Set<T> toSet()
    {
        return toSet(iterator) ;
    }

    public List<T> toList()
    {
        return toList(iterator) ;
    }

    public Iter<T> filter(Filter<T> filter)
    {
        return iter(filter(iterator, filter)) ;
    }

    public boolean every(Filter<T> filter)
    {
        return every(iterator, filter) ;
    }
    
    public boolean some(Filter<T> filter)
    {
        return some(iterator, filter) ;
    }
    
    public Iter<T> removeNulls()
    {
        return filter(new FilterOutNulls<T>()) ;
    }

    public <R> Iter<R> map(Transform<T, R> converter)
    {
        return iter(map(iterator, converter)) ;
    }

    public <R> R reduce(Accumulate<T, R> aggregator)
    {
        return reduce(iterator, aggregator) ;
    }

    public void apply(Action<T> action)
    {
        apply(iterator, action) ;
    }

    public Iter<T> append(Iterator<? extends T> iter)
    {
        // Cosider having a "concat" slot in Iter
        return new Iter<T>(Iterator2.create(iterator, iter)) ;
    }

    public String asString() { return asString(iterator) ; }
    public String asString(String sep) { return asString(iterator, sep) ; }
    
    public Iter<T> distinct()
    {
        return new Iter<T>(distinct(iterator())) ;
    }

    // ---- Iterable
    public Iterator<T>  iterator() { return iterator ; }
    
    // ---- Iterator
    
    //----
    // Could merge in concatenated iterators - if used a lot there is reducable cost.
    // Just putting in a slot is free (?) because objects of one or two slots have
    // the same memory allocation.
    // And .. be an iterator framework for extension
    
    public boolean hasNext()    { return iterator.hasNext() ; }

    public T next()             { return iterator.next() ; }

    public void remove()        { iterator.remove() ; }

    //----
    // Iter class part
    // And ....
    // Could merge in concatenated iterators - if used a lot there is reducable cost.
    // Just putting in a slot is free (?) because objects of one or two slots have
    // the same memory allocation.
    // And .. be an iterator framework for extension
    
    // Or dynamically with a subclass and a static constructor
    // List<Iterator> concatenated = null ; 
    
    public static <T> Iter<T> singletonIter(T item)
    { return new Iter<T>(new SingletonIterator<T>(item)) ; }

    public static <T> Iter<T> nullIter()
    { return new Iter<T>(new NullIterator<T>()) ; }
}

/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */