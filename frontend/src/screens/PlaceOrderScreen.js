import axios from 'axios'
import React, { useEffect } from 'react'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useHistory } from 'react-router'
import {checkAddressFormValid} from '../utils/CheckFormValid'
import {PayPalButton} from 'react-paypal-button-v2';
import { createOrder } from '../_actions/OrderActions'
import Loading from '../components/Loading'
import Error from '../components/Error'

function PlaceOrderScreen(props) {

    const id = props.match.params.id

    const cart = useSelector(state => state.cart)
    const auth = useSelector(state => state.auth)
    const history = useHistory()

    const [err, setErr] = useState({})
    const [addressName, setAddressName] = useState('')
    const [address, setAddress] = useState('')
    const [country, setCountry] = useState('')
    const [city, setCity] = useState('')
    const [zipcode, setZipcode] = useState('')
    const [checkAddress, setCheckAddress] = useState(false)
    const [sdkReady, setSdkReady] = useState(false)
    const [clientId, setClientId] = useState('')
    const [clientSecret, setClientSecret] = useState('')

    const dispatch = useDispatch()


    useEffect(() => {
        if(!cart.checkout){
            history.push(`/`)
        }else{
            if(!window.paypal){
                addPaypalScript()
            }else{
                setSdkReady(true)
            }
        }
    }, [sdkReady, clientId])

    const addPaypalScript = async () =>{
        await axios.get('/paypal',{
            headers : {Authorization : `Bearer ${auth.token}`}
        }).then(res =>{
            setClientId(res.data.clientId)
            setClientSecret(res.data.clientSecret)
            const script = document.createElement('script');
            script.type = "text/javascript";
            script.src = `https://www.paypal.com/sdk/js?client-id=${clientId}`
            script.async = true;
            script.onload = ()=>{
                setSdkReady(true);
            };
            document.body.appendChild(script);
        })

    }

    const handleCheckAddress=(e)=>{
        e.preventDefault()
        const check = checkAddressFormValid(addressName,address,country,city,zipcode)

        if(check.errLength>0) {
            setErr(check.err)
            return
        }else{
            setErr({})
            setCheckAddress(true)
        }
    }

    const subtotal = parseFloat(cart.items?.reduce((a,c)=> a+ c.productPrice*c.qty,0))
    const shipping = subtotal>100 ? parseFloat(0) : parseFloat(5)
    const total = subtotal + shipping 


    const successPaymentHandler = async(details, data)=>{
        const addressInfo = {
            name: addressName,
            country,city,zipcode,address
        }

        const token = `${clientId}:${clientSecret}`;
        const encodedToken = btoa(token)

        await axios.get(`https://api-m.sandbox.paypal.com/v2/checkout/orders/${data.orderID}`,{
            headers : {
            Authorization: "Basic " + encodedToken,
            'Content-type':'application/json'
        }}).then(async(res) =>{
            const orderRequest = {
                userId:auth.user.id,
                shipping,
                paymentMethod:"paypal",
                address:addressInfo,
                transactionId:res.data.purchase_units[0].payments.captures[0].id
            }

            dispatch(createOrder(orderRequest)).then(res=>{
                props.history.push(`/order/${res}`)
            })
        })
    }

    const onError = (error)=>{
        err.payment = error
        setErr(err)
    }

    return (
        <>
        {
            cart.error && cart.error.message && <Error error = {cart.error.message}/>
        }
        {
            cart.Loading && <Loading/>
        }
    
        {
            cart.loading ===false &&
            <div className="placeorder">
                <div className="placeorder_info">
                        <form onSubmit={handleCheckAddress}>
                            <div className="placeorder_address">
                                <div className="placeorder_subtitle">🏠 Shipping Address </div>
                                <div style={{textAlign:"right"}}>
                                    <button>Check Address</button>                                    
                                </div>
                                <input type="text" placeholder="Address Name" value={addressName} onChange={(e)=>setAddressName(e.target.value)}/>
                                {
                                    err.addressName &&
                                    <small>{err.addressName}</small>
                                }
                                <input type="text" placeholder="Address" value={address} onChange={e=>setAddress(e.target.value)}/>
                                {
                                    err.address &&
                                    <small>{err.address}</small>
                                }
                                <input type="text" placeholder="Country" value={country} onChange={e=>setCountry(e.target.value)}/>
                                {
                                    err.country &&
                                    <small>{err.country}</small>
                                }
                                <input type="text" placeholder="City" value={city} onChange={e=>setCity(e.target.value)}/>
                                {
                                    err.city &&
                                    <small>{err.city}</small>
                                }
                                <input type="text" placeholder="Zip Code" value={zipcode} onChange={e=>setZipcode(e.target.value)}/>
                                {
                                    err.zipcode &&
                                    <small>{err.zipcode}</small>
                                }
                            </div>
                        </form>
                        <hr/>
                        <div className="placeorder_payment">
                            <div className="placeorder_subtitle">💸 Payment </div>
                            {
                                checkAddress 
                                ? sdkReady 
                                    ?<PayPalButton amount={total} currency="USD" onSuccess = {successPaymentHandler} onError={onError}></PayPalButton>
                                    : <span>Loading...</span>
                                : <span style={{color:"#787878", textTransform:"uppercase", fontWeight:"800", textAlign:"center"}}>Check Your Address First</span>
                            }
                            {
                                err.payment &&
                                <small>{err.payment}</small>
                            }
                        </div>
                </div>
                <div className="placeorder_summary">
                    <div className="placeorder_items">
                        {
                            cart.items.map((item,index)=>(
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
                            <div style={{fontSize:"15px"}} >{subtotal.toFixed(2)}$</div>
                        </div>
                        <div className="cart_info">
                            <div className="cart_info_label" style={{fontWeight:"600", fontSize:"15px"}}>Shipping</div>
                            <div style={{fontSize:"15px"}}>{shipping.toFixed(2)}$</div>
                        </div>
                        <hr/>
                        <div className="cart_info" style={{marginBottom:"1.6rem"}}>
                            <div className="cart_info_label">Estimated Total</div>
                            <div style={{fontSize:"1.2rem"}}>{total.toFixed(2)}$</div>
                        </div>
                    </div>
                </div>
            </div>
        }
        </>
    )
}

export default PlaceOrderScreen
