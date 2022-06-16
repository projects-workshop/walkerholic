import React from 'react'
import { useState } from 'react'

function Sort({sort,setSort}) {

    return (
        <div className="product_sort">
            <div>
                <span>Sort By : </span>
                <select value={sort} onChange={(e)=>setSort(e.target.value)}>
                    <option value="newest">Newest Arrivals</option>
                    <option value="lowest" >Price: Low to High</option>
                    <option value="highest">Price: High to Low</option>
                    <option value="toprated">Avg. Customer Reviews</option>
                </select>
            </div>
        </div>
    )
}

export default Sort
