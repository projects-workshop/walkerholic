import React from 'react'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import {Link, useHistory} from 'react-router-dom'
import Sidebar from './Sidebar'
import egg from '../../images/egg.png'
import Cart from '../cart/Cart'
import { logout } from '../../_actions/AuthActions'
import Searchbar from './Searchbar'

function Header() {

    const [isOpen, setIsOpen] = useState(false)
    const [isCart, setIsCart] = useState(false)

    const auth = useSelector(state => state.auth)
    const cart = useSelector(state => state.cart)

    const history = useHistory()

    const dispatch = useDispatch()

    const handleLogout = () =>{
        dispatch(logout())
        history.push("/")
    }

    return (
        <>

        <Sidebar setIsOpen={setIsOpen} isOpen={isOpen}/>
        <Cart id={auth.user?.id} isCart={isCart} setIsCart={setIsCart}/>

        <div className="header">
            <div className="header_logo">
                <i className="fas fa-bars" style={{marginRight:'1rem'}} onClick={()=>setIsOpen(true)}></i>
                <i className="fas fa-walking"></i>
                <Link to='/'>walkerholic</Link>
            </div>

            <Searchbar/>

            <div className="header_menu">
                {
                    auth.user&& auth.user.id
                    ?<>
                        {
                            cart.success &&
                            <div className="header_content header_cart">
                                <i className="fas fa-shopping-cart" style={{fontSize:"1.8rem"}} onClick={()=>setIsCart(!isCart)}></i>
                                <div className="header_cart_count">
                                    {cart.items?cart.items.reduce(function add(sum, cur) {return sum+cur.qty},0): 0}
                                </div>
                            </div>
                        }
                        <div className="header_content header_posts">
                            <Link to={`/posts/user/${auth.user.id}`}><i className="far fa-pencil-alt" style={{fontSize:"1.3rem"}}></i></Link>
                        </div>
                        <div className="header_content header_discover">
                            <Link to="/posts/discover"><i className="fas fa-globe" style={{fontSize:"1.3rem"}}></i></Link>
                        </div>

                        <div className="header_content header_activity">
                            <Link to={`/activities/user/${auth.user.id}`}><i className="fas fa-user-astronaut" style={{fontSize:"1.3rem"}}></i></Link>
                        </div>

                        <div className="header_content header_order">
                            <Link to={`/orderlist/user/${auth.user.id}`}><img src={egg} alt="" style={{width:"30px"}} /></Link>
                        </div>

                        <div className="header_content header_user">
                            <Link to={`/user/${auth.user.id}`}>{auth.user.firstname}</Link>
                        </div>

                        {
                            auth.user.role === "ADMIN"
                            ?<div className="header_content">    
                                <div className="dropdown">
                                    <span> <i className="fas fa-cog" style={{fontSize:"1.8rem"}}></i></span>
                                    <div className="dropdown-menu dropdown-menu-right" >
                                        <Link className="dropdown-item" to="/userlist">User List</Link>
                                        <Link className="dropdown-item" to="/orderlist" >Order List</Link>
                                        <Link className="dropdown-item" to="/productlist" >Product List</Link>
                                    </div>
                                </div>
                            </div>
                            :auth.user.role ==="SELLER" &&
                            <div className="header_content">
                                <div className="dropdown">
                                    <span style={{fontSize:"1.8rem", fontWeight:"800"}}>$$</span>
                                    <div className="dropdown-menu dropdown-menu-right" >
                                        <Link className="dropdown-item" to={`/products/${auth.user.id}`}>Products</Link>
                                        <Link className="dropdown-item" to={`/orderlist/${auth.user.id}`} >OrderList</Link>
                                    </div>
                                </div>
                            </div>
                        }
                        <div className="header_content header_logout" onClick={handleLogout}>
                            <i className="fas fa-hand-paper" style={{fontSize:"1.3rem"}}></i>
                        </div>
                        </>
                    :<div className="header_content header_login">
                        <Link to='/signin'><i className="far fa-hand-paper" style={{fontSize:"1.3rem", fontWeight:'400'}}></i></Link>
                    </div>
               }

                
            </div>
        </div>


        </>
    )
}

export default Header
