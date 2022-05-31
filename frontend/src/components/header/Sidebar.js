import React from 'react'
import { useSelector } from 'react-redux'
import {Link, useHistory} from 'react-router-dom'
import { auth } from '../../_actions/AuthActions'

function Sidebar({isOpen,setIsOpen}) {

    const history = useHistory();

    const handlePage = (page) =>{
        if(page){
            history.push(`/${page}`)
        }else{
            history.push('/')
        }
        setIsOpen(false)
    }

    const auth = useSelector(state => state.auth)
    return (
        <div className={isOpen? "sidebar sideopen": "sidebar sideclose"}>
            <div className="sidebar_container">
                <div className="sidebar_logo">
                    <div className="header_logo" onClick={()=>handlePage()}>
                        <i className="fas fa-bars" style={{marginRight:'1rem'}}></i>
                        <i className="fas fa-male" ></i>
                        {/* <Link to='/'>walkerholic</Link> */}
                        <span className="sidebar_page">walkerholic</span>
                    </div>
                    <div style={{fontSize:'3rem', fontWeight:'800', cursor:'pointer'}} onClick={()=>setIsOpen(false)}>
                        &times;
                    </div>
                </div>
                <div className="sidebar_items">
                    <div className="sidebar_item">
                        <div className="sidebar_item_name" onClick={()=>handlePage('posts')}>
                            {/* <Link to='/posts'>Post</Link> */}
                            <span className="sidebar_page">Post</span>
                        </div>
                        <div className="sidebar_description">
                            Checkout our planetsaver's latest moment
                        </div>
                    </div>
                    <div className="sidebar_item">
                        <div className="sidebar_item_name" onClick={()=>handlePage('products')}>
                            {/* <Link to="/products">Product</Link> */}
                            <span className="sidebar_page">Product</span>
                        </div>
                        <div className="sidebar_description">
                            Explore ecofriendly products
                        </div>
                    </div>
                    <div className="sidebar_item">
                        <div className="sidebar_item_name" onClick={()=>handlePage('activities')}>
                            {/* <Link to="/activities">Activity</Link> */}
                            <span className="sidebar_page">Activity</span>
                        </div> 
                        <div className="sidebar_description">
                            Help planet's health with your power
                        </div>
                    </div>
                    <div className="sidebar_item">
                        <div className="sidebar_item_name" onClick={()=>handlePage('about')}>
                            {/* <Link>About</Link> */}
                            <span className="sidebar_page">About</span>
                        </div>
                        <div className="sidebar_description">
                            Our vision
                        </div>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default Sidebar
