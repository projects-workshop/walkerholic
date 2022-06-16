import React from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import Category from '../components/product/Category'
import ProductCard from '../components/product/ProductCard'
import SellerCard from '../components/product/SellerCard'
import Sort from '../components/product/Sort'
import { getProducts, getSellerProducts } from '../_actions/ProductActions'
import { RESET_SELLER } from '../_constants/ProductConstants'

function ProductScreen(props) {

    const sellerId = props.match.params.seller
    const keyword = props.location.search.substr(9)

    const [page, setPage] = useState(1)
    const [category, setCategory] = useState('')
    const [sort, setSort] = useState('createdAt')
    
    const dispatch = useDispatch()

    const products = useSelector(state => state.products)  
    const pages = [...Array(products.products?.totalPage).keys()]

    useEffect(() => {
        if(sellerId){
            dispatch(getSellerProducts(sellerId,page, sort,category,keyword))
        }else{
            dispatch({
                type:RESET_SELLER
            })

            dispatch(getProducts(page, sort, category,keyword))
        }
    }, [dispatch, page, sort, category, keyword, sellerId])

    return (
        <div className="productscreen">
            {
                products.error && products.error.message && <Error error = {products.error.message}/>
            }
            {
                products.Loading && <Loading/>
            }

            {   
                products.products &&
                <div>
                    <Category category={category} setCategory={setCategory}/>

                    <Sort setSort={setSort}/>
                    {
                        sellerId && 
                        products.products &&
                        <SellerCard seller={products.seller}/>
                    }

                    <ProductCard products={products.products}/>
                </div>
            }

            {
                <nav aria-label="Pagination">
                    <ul className="pagination">
                        <li className="page-item" style={{borderRadius:"10px 0px 0px 10px"}}>
                            <i className="fas fa-backward" onClick={()=>setPage(1)}></i>
                        </li>
                        {
                            pages.map((x, index)=>(
                                <li key={index} className={`page-item ${page===x+1 && 'page_active'}`} onClick={()=>setPage(x+1)}>{x+1}</li>
                            ))
                        }
                        <li className="page-item" style={{borderRadius:"0px 10px 10px 0px"}}>
                            <i className="fas fa-forward" onClick={()=>setPage(pages[-1])}></i>
                        </li>
                    </ul>
                </nav>
            }

        </div>
    )
}

export default ProductScreen
