import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useHistory } from 'react-router'
import { CHECKOUT } from '../../_constants/OrderConstants'
import CartProductCard from './CartProductCard'

function Cart({id, isCart, setIsCart}) {

    const cart = useSelector(state => state.cart)

    const dispatch = useDispatch()
    const history = useHistory()

    const subtotal = cart.items? parseFloat(cart.items?.reduce((a,c)=> a+ c.productPrice*c.qty,0)) : parseFloat(0)
    const shipping = subtotal>100 ? parseFloat(0) : parseFloat(5)
    const total = subtotal + shipping 

    const handleCheckout = () =>{
        dispatch({
            type:CHECKOUT
        })
        setIsCart(false)
        return history.push(`/placeorder/${id}`)
    }

    return (
        <>
        {
            cart.loading===false &&
            <div className={`cartscreen ${isCart ? 'cartopen':'cartclose'}`}>                
                <div className="cart_products">
                    <div className="cart_information">
                        <div style={{fontSize:"13px", transform:"translateY(-15px)"}}>Free shipping for more than 100$.</div>
                        <div style={{fontSize:"2.6rem", color:"#969696",cursor:"pointer"}} onClick={()=>setIsCart(false)}>&times;</div>
                        {/* <div style={{textAlign:"right", fontSize:"9px", fontWeight:"600", marginRight:"1.5rem"}}>Total Items : {cart.items.reduce((a, c)=> a+c.qty, 0)}</div> */}
                    </div>
                    <div className="cart_products_container">
                        {
                            (!cart.items ||cart.items.length===0 )
                            ? <span>No Items.</span>
                            :   cart.items.map((item,index)=>(
                                    <CartProductCard product={item} key={index} />
                                ))
                        }
                    </div>
                </div>

                <div className="cart_info_container">
                    <div className="cart_info">
                        <div className="cart_info_label">Subtotal</div>
                        <div>{subtotal.toFixed(2)}$</div>
                    </div>
                    <div className="cart_info">
                        <div className="cart_info_label">Shipping</div>
                        <div>{shipping.toFixed(2)}$</div>
                    </div>
                    <div className="cart_info" style={{marginBottom:"1.6rem"}}>
                        <div className="cart_info_label">Estimated Total</div>
                        <div>{total.toFixed(2)}$</div>
                    </div>
                    <div className="form_button">
                        <button onClick={handleCheckout}>Go To Checkout</button>
                    </div>
                </div>

            </div>
            
        }
        </>
    )
}

export default Cart
