import React, { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux'
import { deleteCartItem, deleteOrderItem, updateQty } from '../../_actions/OrderActions'
import QuantityInput from '../QuantityInput'

function CartProductCard({product}) {

    const [qty, setQty] = useState(product.qty)

    const dispatch = useDispatch()

    useEffect(() => {
        if(qty!==product.qty){
            dispatch(updateQty(product.id, qty))
        }
    }, [qty])

    const handleDelete = () =>{
        dispatch(deleteCartItem(product.id))
    }

    return (
        <>
            <div className="cart_productcard">
                <div className="cart_product_image">
                    <img src={product.productImageUrl} alt="" />
                </div>
                <div className="cart_product_info">
                    <div>
                        <div style={{fontSize:"1.3rem", fontWeight:"600"}}>
                            {product.productName}
                        </div>
                        <div style={{fontSize:"1.1rem",fontStyle:"italic"}}>
                            {parseFloat(product.productPrice* qty).toFixed(2)} $
                        </div>
                    </div>
                    <QuantityInput qty={qty} setQty={setQty} stock={product.stock}/>
                </div>
                <div className="cart_product_delete">
                    <div style={{marginBottom:"1rem"}}>
                        <span onClick={handleDelete} >&times;</span>
                    </div>

                </div>
            </div>
        </>
    )
}

export default CartProductCard
