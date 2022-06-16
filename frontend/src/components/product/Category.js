import axios from 'axios'
import React from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import { useHistory } from 'react-router'

function Category({category,setCategory}) {

    const history = useHistory()

    const [categories, setCategories] = useState([])

    const getCategories = async() =>{
        await axios.get('/categories').then(res=>{
            setCategories(res.data)
        })
    }

    useEffect(() => {
        getCategories()
    }, [])


    return (
        <div className="product_category">
            <div>
                <span className={`product_category_name ${category==='' && 'product_category_active'}`} onClick={()=>setCategory('')}>
                    ALL 
                </span>
                {
                    categories.length>0 &&
                    <span>|</span>
                }
            </div>
        {
            categories.map((c,index)=>(
                <div key={index}>
                    <span className={`product_category_name ${category===c && 'product_category_active'}`} key={index} onClick={()=>setCategory(c)}>
                        {c} 
                    </span>
                    {
                        index<categories.length-1 &&
                        <span>|</span>
                    }
                </div>
            ))
        }
        </div>
    )
}

export default Category
