import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import earth from '../../images/earth.svg'
import { addCart, createCart } from '../../_actions/OrderActions'
import Rating from './Rating'

function ProductCard({products}) {
    
    const dispatch = useDispatch()

    const cart = useSelector(state => state.cart)

    const handleAddToCart = (product) =>{
        if(cart.id){
            dispatch(addCart(1, product.id,cart.id))
        }else{
            dispatch(createCart()).then(res=>{
                dispatch(addCart(1, product.id, res))
            })
        }
    }
    return (
        <div className="productcard_container">
            {
                products.map((product,index)=>(
                    <div className="productcard" key={index}>
                        <Link to={`/product/${product.id}`}>
                            <div className="productcard_image">
                                <img src={product.imagesUrl[0]} alt="" />
                            </div>
                            <div className="productcard_product_info">
                            <div className="productcard_name">
                                <div>{product.name}</div>
                                <div className="productcard_stock">
                                {
                                    product.stock>0 
                                    ? <span className="in_stock" onClick={()=>handleAddToCart(product)} >&nbsp;In Stock&nbsp;</span>
                                    : <span className="out_of_stock">&nbsp;Out of Stock&nbsp;</span>
                                }
                                </div>
                            </div>
                            <div className="productcard_price">
                                <div >&nbsp;{parseFloat(product.price).toFixed(2)}$&nbsp;</div>
                                <Rating rating={product.average}/>
                            </div>
                            </div>
                        </Link>

                    </div>
                ))
            }

        </div>
    )
}

export default ProductCard
