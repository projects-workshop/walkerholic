import React from 'react'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import { cancelOrder, getOrder } from '../_actions/OrderActions'

function OrderScreen(props) {

    const id = props.match.params.id

    const dispatch = useDispatch()

    const order = useSelector(state => state.order)
    const auth = useSelector(state => state.auth )

    useEffect(() => {
        if(auth.user && id) {
            dispatch(getOrder(id))
        }
    }, [auth.user, id])

    const handleCancelOrder = (e) =>{
        e.preventDefault()
        dispatch(cancelOrder(id, order.transactionId))
    }

    return (
        <div>
            {
                order.error && order.error.message && <Error error = {order.error.message}/>
            }
            {
                order.Loading && <Loading/>
            }
            {
                order.loading===false &&

                <div className="placeorder">
                    <div className="placeorder_info">
                                <div className="placeorder_address">
                                    <div className="placeorder_subtitle">🏠 Shipping Address </div>
                                    <div className="order_info">
                                        <label>Address Name : </label>
                                        <span>{order.address.name}</span>
                                    </div>
                                    <div className="order_info">
                                        <label>Address : </label>
                                        <span>{order.address.address}</span>
                                    </div>
                                    <div className="order_info">
                                        <label>Country : </label>
                                        <span>{order.address.country}</span>
                                    </div>
                                    <div className="order_info">
                                        <label>City : </label>
                                        <span>{order.address.city}</span>
                                    </div>
                                    <div className="order_info">
                                        <label>ZipCode : </label>
                                        <span>{order.address.zipcode}</span>
                                    </div>
                                </div>
                            <hr/>
                            <div className="placeorder_payment">
                                <div className="placeorder_subtitle">💸 Payment </div>
                                <div className="order_info">
                                    <label>Payment Method : </label>
                                    <span style={{color:"#0d72bf", backgroundColor:"#e3b13b", borderRadius:"3px", border:"1px solid #e3b13b",paddingLeft:"10px", paddingRight:"10px"}}>
                                        {order.paymentMethod.toUpperCase()}
                                    </span>
                                </div>
                            </div>
                            <hr/>
                            <div className="placeorder_delivery">
                                <div className="placeorder_subtitle">🛵 Delivery </div>
                                <div className="order_info">
                                    <label>Delivery Status : </label>
                                    <span style={order.delivered? {color:"#0c754b"}:{color:"#b33f09"}}>
                                        {order.delivered? order.deliveredAt : "NOT YET"}
                                    </span>
                                </div>
                            </div>
                            <hr/>
                            <div className="placeorder_delivery">
                                <div className="placeorder_subtitle">✨ Order </div>
                                <div className="order_info">
                                    <label>Order Status : </label>
                                    <span style={order.orderStatus==="ORDER"? {color:"#0c754b"}:{color:"#b33f09"}}>
                                        {order.orderStatus}
                                    </span>
                                </div>
                            </div>

                    </div>
                    <div className="placeorder_summary">
                        <div className="placeorder_items">
                            {
                                order.items.map((item,index)=>(
                                    <div key={index} className="placeorder_item">
                                        <div style={{display:"flex",justifyContent:"flex-start", alignItems:"center"}}>
                                            <div className="placeorder_item_qty">
                                                {item.qty}
                                            </div>
                                            <div className="placeorder_item_image">
                                                <img src={item.productImageUrl} alt="" />
                                            </div>
                                            <div className="placeorder_item_name">
                                                {item.productName}
                                            </div>
                                        </div>
                                        <div className="placeorder_item_price">
                                            {parseFloat(item.productPrice* item.qty).toFixed(2)} $
                                        </div>
                                    </div>
                                ))
                            }
                        </div>
                        <hr/>
                        <div className="placeorder_price">
                            <div className="cart_info">
                                <div className="cart_info_label" style={{fontWeight:"600", fontSize:"15px"}}>Subtotal</div>
                                <div style={{fontSize:"15px"}} >{order.total.toFixed(2)}$</div>
                            </div>
                            <div className="cart_info">
                                <div className="cart_info_label" style={{fontWeight:"600", fontSize:"15px"}}>Shipping</div>
                                <div style={{fontSize:"15px"}}>{order.shipping.toFixed(2)}$</div>
                            </div>
                            <hr/>
                            <div className="cart_info" style={{marginBottom:"1.6rem"}}>
                                <div className="cart_info_label">Total</div>
                                <div style={{fontSize:"1.2rem"}}>{(order.total+order.shipping).toFixed(2)}$</div>
                            </div>
                            <div>
                                {
                                    order.orderStatus !== "CANCEL" && !order.delivered &&
                                    <button onClick={handleCancelOrder}>Cancel Order</button>
                                } 
                            </div>
                        </div>
                    </div>
                </div>
            }
        </div>
    )
}

export default OrderScreen
