import React from 'react'
import basicProfile from '../../images/basicProfile.svg'
import {levelIconShow} from '../../utils/MediaShow'
import SellerExclude from '../../images/SellerExclude2.png'
import {Link} from 'react-router-dom'

function SellerCard({seller}) {
    return (
    <div className="seller">
        <Link to={`/user/${seller.id}`} style={{display:"flex", justifyContent:"space-between",alignItems:"center"}}>
        <div className="seller_image">
            <img src={seller.imageUrl ? seller.imageUrl : basicProfile} alt="" />
            <div className="seller_image_tool">
                <img src={SellerExclude} alt="" />
            </div>
        </div>
        <div className="seller_info">
            <div className="seller_name">
                <span>{seller.fullname}</span>
                {levelIconShow(seller.level)}
            </div>
            <div >
                {seller.email}
            </div>
            <div>
                {seller.phoneNumber}
            </div>
            <div style={{color:"#616161"}}>
                {seller.description}
            </div>
        </div>
        </Link>
    </div>
    )
}

export default SellerCard
